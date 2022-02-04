package com.hzsh.zhks.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.PeiZhiService;
import com.hzsh.util.ExcelUtil;
import com.hzsh.zhks.mapper.KaiShiLiRunMapper;
import com.hzsh.zhks.model.KaiShiLiRun;
import com.hzsh.zhks.model.KaiShiLiRun;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 中海开氏-利润   [ZHKS_LI_RUN]
 */
@Service
public class KaiShiLiRunServiceImpl extends ServiceImpl<KaiShiLiRunMapper, KaiShiLiRun> implements KaiShiLiRunService {

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    private final String CATEGORY = "利润表";



    /**
     * 从Excel导入数据
     */
    @Override
    public List<KaiShiLiRun> importKaiShiLiRun(MultipartFile file) {
        // 1. 从文件中提取数据
        List<KaiShiLiRun> list = _从Excel文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [ZHKS_LI_RUN]
        String month = list.get(0).getMonthString();
        for (KaiShiLiRun one : list) {
            KaiShiLiRun dbOne = getUnique(month, one.code);

            if (dbOne == null) {
                super.save(one);
            }
            else {
                one.id = dbOne.id;
                super.updateById(one);
            }
        }

        return list;
    }

    /**
     * 指定日期的数据
     */
    @Override
    public List<KaiShiLiRun> defaultList(String month) {
        LambdaQueryWrapper<KaiShiLiRun> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(KaiShiLiRun::getMonth, DateTime.parse(month).toDate())
                .orderByAsc(KaiShiLiRun::getSort);
        List<KaiShiLiRun> list = baseMapper.selectList(queryWrapper);


        // 查询 [HZSH_PEI_ZHI] 获取对应公式
        for (KaiShiLiRun one : list) {
            PeiZhi peiZhi = peiZhiService.getUnique(one.code);
            one.peiZhi = peiZhi;
        }

        // 排除隐藏的行
        list = list.stream().filter(i -> i.peiZhi != null && i.peiZhi.isHidden == false).collect(Collectors.toList());

        return list;
    }






    private List<KaiShiLiRun> _从Excel文件中提取数据(MultipartFile file) {

        // 1. 打开文件
        Workbook wb;
        wb = excelUtil.openExcel(file);
        if (wb == null) {
            return null;
        }


        // 2. 获取指定表格
        Sheet sheet = wb.getSheet(CATEGORY);


        // 3. 获取月份
        String month = "2021-08-01";


        // 4. 定义数据行开始的位置
        int 数据行开始的位置 = 3;


        // 5. 获取数据
        int
                项目CellIndex = 0,
                某月预测CellIndex = 1,
                备注CellIndex = 2;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<KaiShiLiRun> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                KaiShiLiRun one = new KaiShiLiRun();
                one.month = DateTime.parse(month).toDate();
                one.code = PeiZhi.CATEGORY_开氏_利润表 + new DecimalFormat("000000").format(codeNumber++);
                one.category = "开氏" + CATEGORY;
                one.name = name.replace(" ", "").trim();
                one.预测金额 = excelUtil.getDouble(row, 某月预测CellIndex);
                one.remark = excelUtil.getString(row, 备注CellIndex);
                one.sort = row.getRowNum();
                one.isHidden = row.getZeroHeight();


                fetchDataList.add(one);
            }
        } catch (Exception exception) {
            String msg = "\n\n" + currentRowNum + " Row Error! \n" + exception + "\n\n";
            System.out.println(msg);
            throw new RuntimeException(msg);
        }


        // 6. 关闭文件
        try {
            wb.close();
        } catch (Exception exception) {
            throw new RuntimeException("关闭文件失败");
        }

        //
        return fetchDataList;
    }






    /**
     * 获取唯一对象
     */
    @Override
    public KaiShiLiRun getUnique(String month, String code) {
        LambdaQueryWrapper<KaiShiLiRun> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(KaiShiLiRun::getMonth, DateTime.parse(month).toDate())
                .eq(KaiShiLiRun::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }



    /**
     * 返回导数模板
     */
    @Override
    public HSSFSheet 返回导数模板(HSSFWorkbook workbook) {
        HSSFCellStyle 标题样式 = excelUtil.标题样式(workbook);
        HSSFCellStyle 表头样式 = excelUtil.表头样式(workbook);
        HSSFCellStyle 数据行样式 = excelUtil.数据行样式(workbook);
        HSSFCellStyle 数据行加粗样式 = excelUtil.数据行加粗样式(workbook);


        // sheet
        HSSFSheet sheet = workbook.createSheet(CATEGORY);


        //#region 1. 标题行
        HSSFRow 标题行 = sheet.createRow(0);
        HSSFCell 标题Cell = 标题行.createCell(0);
        标题Cell.setCellValue("开氏 - 全产全销售利润");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                项目HeaderCell = 表头行.createCell(0),
                预测金额HeaderCell = 表头行.createCell(1),
                备注HeaderCell = 表头行.createCell(2),
                编码HeaderCell = 表头行.createCell(3);
        项目HeaderCell.setCellValue("项目");
        预测金额HeaderCell.setCellValue("预测金额(万元)");
        备注HeaderCell.setCellValue("备注");
        编码HeaderCell.setCellValue("编码");
        表头行.forEach(cell -> cell.setCellStyle(表头样式));
        //#endregion


        //#region 3. 数据行
        List<PeiZhi> peiZhiList = peiZhiService.getByCategory("开氏" + CATEGORY);
        int 行索引 = 2;
        for (PeiZhi peiZhi : peiZhiList) {
            HSSFRow row = sheet.createRow(行索引++);

            // 项目名称列
            String name = peiZhi.name;
            if (peiZhi.cengCi != null) {
                System.out.println();
                String 缩进 = "";
                for (int i = 0; i < peiZhi.cengCi; i++) {
                    缩进 += " ";
                }
                name = 缩进 + name;
            }
            HSSFCell 项目Cell = row.createCell(0);
            项目Cell.setCellValue(name);

            HSSFCell 预测金额Cell = row.createCell(1);

            HSSFCell 备注Cell = row.createCell(2);
            备注Cell.setCellType(CellType.STRING);

            // 编码
            HSSFCell 编码Cell = row.createCell(3);
            编码Cell.setCellType(CellType.STRING);
            编码Cell.setCellValue(peiZhi.code);

            // 设置单元格样式
            row.forEach(cell -> cell.setCellStyle(数据行样式));

            // 隐藏的行
            if (peiZhi.isHidden) {
                row.setZeroHeight(true);
            }

            // 加粗的行
            if (peiZhi.isBold) {
                row.forEach(cell -> cell.setCellStyle(数据行加粗样式));
            }
        }
        //#endregion


        //#region 4. 设置公式
        //#endregion





        // 设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 50 * 256);
        sheet.setColumnWidth(3, 0);


        // 为合并的单元格设置边框
        excelUtil.setBordersToMergedCells(sheet);


        // 合并「备注」单元格
        sheet.addMergedRegion(new CellRangeAddress(2, sheet.getLastRowNum(), 2, 2));
        sheet.getRow(2).getCell(2).setCellStyle(excelUtil.合并的备注样式(workbook));


        return sheet;
    }


    /**
     * 从导数模板导入数据
     */
    @Override
    public int 从导数模板导入数据(MultipartFile file) {
        // 1. 打开文件
        Workbook wb;
        wb = excelUtil.openExcel(file);
        if (wb == null) {
            return - 1;
        }


        // 2. 获取指定表格
        Sheet sheet = wb.getSheet(CATEGORY);


        // 3. 指定日期
        final Date month = DateTime.now().dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();


        // 4. 定义数据行开始的位置
        int 数据行开始的位置 = 2;


        // 5. 获取数据
        int
                项目CellIndex = 0,
                预测金额CellIndex = 1,
                备注CellIndex = 2,
                编码CellIndex = 3;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<KaiShiLiRun> fetchDataList = new ArrayList<>();
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                KaiShiLiRun one = new KaiShiLiRun();
                one.month = month;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = "开氏" + CATEGORY;
                one.name = name.trim();
                one.预测金额 = excelUtil.getDouble(row, 预测金额CellIndex);
                one.remark = excelUtil.getString(row, 备注CellIndex);
                one.sort = row.getRowNum() + 1;

                fetchDataList.add(one);
            }
        } catch (Exception exception) {
            String msg = "\n\n" + currentRowNum + " Row Error! \n" + exception + "\n\n";
            System.out.println(msg);
            throw new RuntimeException(msg);
        }


        // 6. 关闭文件
        try {
            wb.close();
        } catch (Exception exception) {
            throw new RuntimeException("关闭文件失败");
        }


        // 7. 新增或修改数据
        for (KaiShiLiRun one : fetchDataList) {
            insertOrUpdate(one);
        }


        //
        return fetchDataList.size();
    }





    @Override
    public boolean insertOrUpdate(KaiShiLiRun one) {
        String month = one.getMonthString();

        KaiShiLiRun dbOne = getUnique(month, one.code);

        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.id;
            return super.updateById(one);
        }
    }

}
