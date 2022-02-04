package com.hzsh.zhks.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.PeiZhiService;
import com.hzsh.util.ExcelUtil;
import com.hzsh.zhks.mapper.KaiShiFeiYongMapper;
import com.hzsh.zhks.model.KaiShiFeiYong;
import com.hzsh.zhks.model.KaiShiFeiYong;
import com.hzsh.zhks.model.KaiShiFeiYong;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中海开氏-费用  [ZHKS_FEI_YONG]
 */
@Service
public class KaiShiFeiYongServiceImpl extends ServiceImpl<KaiShiFeiYongMapper, KaiShiFeiYong> implements KaiShiFeiYongService {

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    private final String CATEGORY = "费用";


    /**
     * 从Excel导入数据
     */
    @Override
    public List<KaiShiFeiYong> importKaiShiFeiYong(MultipartFile file) {
        // 1. 从文件中提取数据
        List<KaiShiFeiYong> list = getExcelDate(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [ZHKS_XIAOSHOU_SHOURU]
        String month = list.get(0).getMonthString();
        for (KaiShiFeiYong one : list) {
            KaiShiFeiYong dbOne = getUnique(month, one.code);

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



    @Override
    public List<KaiShiFeiYong> defaultList(String month) {
        LambdaQueryWrapper<KaiShiFeiYong> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(KaiShiFeiYong::getMonth, DateTime.parse(month).toDate())
                .orderByAsc(KaiShiFeiYong::getSort);
        List<KaiShiFeiYong> list = baseMapper.selectList(queryWrapper);


        // 查询 [HZSH_PEI_ZHI] 获取对应公式
        for (KaiShiFeiYong one : list) {
            PeiZhi peiZhi = peiZhiService.getUnique(one.code);
            one.peiZhi = peiZhi;
        }

        // 排除隐藏的行
        list = list.stream().filter(i -> i.peiZhi != null && i.peiZhi.isHidden == false).collect(Collectors.toList());

        return list;
    }


    private List<KaiShiFeiYong> getExcelDate(MultipartFile file) {
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
        int startRow = 3;


        // 5. 获取数据
        int
                序号CellIndex = 0,
                项目CellIndex = 1,
                合计CellIndex = 2,
                制造费用CellIndex = 3,
                管理费用CellIndex = 4,
                财务费用CellIndex = 5,
                销售费用CellIndex = 6;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<KaiShiFeiYong> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < startRow) {
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
                KaiShiFeiYong one = new KaiShiFeiYong();
                one.month = DateTime.parse(month).toDate();
                one.code = PeiZhi.CATEGORY_开氏_费用 + new DecimalFormat("000000").format(codeNumber++);
                one.category = "开氏" + CATEGORY;
                one.name = name.trim();
                one.合计 = excelUtil.getDouble(row, 合计CellIndex);
                one.制造费用 = excelUtil.getDouble(row, 制造费用CellIndex);
                one.管理费用 = excelUtil.getDouble(row, 管理费用CellIndex);
                one.财务费用 = excelUtil.getDouble(row, 财务费用CellIndex);
                one.销售费用 = excelUtil.getDouble(row, 销售费用CellIndex);
                one.sort = row.getRowNum();
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

        return fetchDataList;

    }



    /**
     * 获取唯一对象、按照月、名称
     */
    @Override
    public KaiShiFeiYong getUnique(String month, String code) {
        LambdaQueryWrapper<KaiShiFeiYong> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(KaiShiFeiYong::getMonth, DateTime.parse(month).toDate())
                .eq(KaiShiFeiYong::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }



    @Override
    public int edit(String name, String value, Long pk) {
        if (StringUtils.isEmpty(value)) {
            // ①. 若设置空值, 数字类型的值需要这里特别处理. (字符串类型也适用)
            Field field = Arrays.stream(KaiShiFeiYong.class.getDeclaredFields()).filter(i -> i.getName().equals(name)).findFirst().get();
            TableField tableField = field.getAnnotation(TableField.class);
            String 数据库列名 = tableField == null ? name : tableField.value();
            UpdateWrapper<KaiShiFeiYong> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("id", pk)
                    .set(数据库列名, value);
            update(updateWrapper);
        }
        else {
            // ②. 非空值的修改
            Map map = new HashMap();
            map.put(name, value);
            KaiShiFeiYong one = JSON.parseObject(JSON.toJSONString(map), KaiShiFeiYong.class);
            one.id = getById(pk).id;
            updateById(one);
        }

        return 1;
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
        标题Cell.setCellValue("开氏 - 费用");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                项目HeaderCell = 表头行.createCell(0),
                合计HeaderCell = 表头行.createCell(1),
                制造费用HeaderCell = 表头行.createCell(2),
                管理费用HeaderCell = 表头行.createCell(3),
                财务费用HeaderCell = 表头行.createCell(4),
                销售费用HeaderCell = 表头行.createCell(5),
                编码HeaderCell = 表头行.createCell(6);
        项目HeaderCell.setCellValue("项目");
        合计HeaderCell.setCellValue("合计");
        制造费用HeaderCell.setCellValue("制造费用");
        管理费用HeaderCell.setCellValue("管理费用");
        财务费用HeaderCell.setCellValue("财务费用");
        销售费用HeaderCell.setCellValue("销售费用");
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

            HSSFCell 合计Cell = row.createCell(1);

            HSSFCell 制造费用Cell = row.createCell(2);

            HSSFCell 管理费用Cell = row.createCell(3);

            HSSFCell 财务费用Cell = row.createCell(4);

            HSSFCell 销售费用Cell = row.createCell(5);

            // 编码
            HSSFCell 编码Cell = row.createCell(6);
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
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 0);


        // 为合并的单元格设置边框
        excelUtil.setBordersToMergedCells(sheet);


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
                合计CellIndex = 1,
                制造费用CellIndex = 2,
                管理费用CellIndex = 3,
                财务费用CellIndex = 4,
                销售费用CellIndex = 5,
                编码CellIndex = 6;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<KaiShiFeiYong> fetchDataList = new ArrayList<>();
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
                KaiShiFeiYong one = new KaiShiFeiYong();
                one.month = month;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = "开氏" + CATEGORY;
                one.name = name.trim();
                one.合计 = excelUtil.getDouble(row, 合计CellIndex);
                one.制造费用 = excelUtil.getDouble(row, 制造费用CellIndex);
                one.管理费用 = excelUtil.getDouble(row, 管理费用CellIndex);
                one.财务费用 = excelUtil.getDouble(row, 财务费用CellIndex);
                one.销售费用 = excelUtil.getDouble(row, 销售费用CellIndex);
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
        for (KaiShiFeiYong one : fetchDataList) {
            insertOrUpdate(one);
        }


        //
        return fetchDataList.size();
    }




    @Override
    public boolean insertOrUpdate(KaiShiFeiYong one) {
        String month = one.getMonthString();

        KaiShiFeiYong dbOne = getUnique(month, one.code);

        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.id;
            return super.updateById(one);
        }
    }


}
