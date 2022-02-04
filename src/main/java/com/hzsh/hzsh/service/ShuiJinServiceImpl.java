package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.ShuiJinMapper;
import com.hzsh.hzsh.model.ShuiJin;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.model.ShuiJin;
import com.hzsh.hzsh.model.ShuiJin;
import com.hzsh.util.ExcelUtil;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 税金 - HZSH_SHUIJIN
 */
@Service
public class ShuiJinServiceImpl extends ServiceImpl<ShuiJinMapper, ShuiJin> implements ShuiJinService {

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    @Resource
    private GongShiService gongShiService;

    private final String CATEGORY = "税金及附加";





    /**
     * <h2>从Excel导入数据</h2>
     */
    @Override
    public List<ShuiJin> importShuiJin(MultipartFile file) {
        // 1. 从文件中提取数据
        List<ShuiJin> list = _从Excel文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [HZSH_SHUIJIN]
        String day = list.get(0).getDayString();
        for (ShuiJin one : list) {
            insertOrUpdate(one);
        }


        return list;
    }



    /**
     * 指定日期的数据
     */
    @Override
    public List<ShuiJin> defaultList(String day) {
        LambdaQueryWrapper<ShuiJin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ShuiJin::getDay, DateTime.parse(day).toDate())
                .orderByAsc(ShuiJin::getSort);
        List<ShuiJin> list = baseMapper.selectList(queryWrapper);


        // 查询 [HZSH_PEI_ZHI] 获取对应公式
        for (ShuiJin one : list) {
            PeiZhi peiZhi = peiZhiService.getUnique(one.code);
            one.peiZhi = peiZhi;
        }

        // 排除隐藏的行
        list = list.stream().filter(i -> i.peiZhi != null && i.peiZhi.isHidden == false).collect(Collectors.toList());


        return list;
    }




    /**
     * 获取唯一对象
     */
    @Override
    public ShuiJin getUnique(String day, String code) {
        LambdaQueryWrapper<ShuiJin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ShuiJin::getDay, DateTime.parse(day).toDate())
                .eq(ShuiJin::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }






    @Override
    public int edit(String name, String value, Long pk) {
        gongShiService.修改单元格数据(CATEGORY, pk, name, value);
        return 1;
    }





    @Override
    public int 复制最近日期的数据(String 待保存日期) {
        // 1. 最近有数据的日期
        QueryWrapper<ShuiJin> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("MAX(DAY)").lambda().ne(ShuiJin::getDay, DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate());
        Map<String, Object> map = getMap(queryWrapper1);
        Date date = new DateTime(map.get("MAX(DAY)")).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<ShuiJin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ShuiJin::getDay, date);
        List<ShuiJin> list = list(queryWrapper2);


        // 3. 复制
        List<ShuiJin> 待复制列表 = new ArrayList<>();
        for (ShuiJin one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            ShuiJin newOne = JSON.parseObject(jsonObject.toJSONString(), ShuiJin.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (ShuiJin one : 待复制列表) {
            insertOrUpdate(one);
        }


        return 待复制列表.size();
    }



    @Override
    public int 测试用_复制底稿数据到今天() {
        // 1. 底稿数据日期
        final String 底稿数据日期 = "2020-09-01";
        Date date = DateTime.parse(底稿数据日期).toDate();
        final String 待保存日期 = DateTime.now().toString("yyyy-MM-dd");


        // 2. 日期对应的数据
        LambdaQueryWrapper<ShuiJin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ShuiJin::getDay, date);
        List<ShuiJin> list = list(queryWrapper2);


        // 3. 复制
        List<ShuiJin> 待复制列表 = new ArrayList<>();
        for (ShuiJin one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            ShuiJin newOne = JSON.parseObject(jsonObject.toJSONString(), ShuiJin.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (ShuiJin one : 待复制列表) {
            insertOrUpdate(one);
        }


        return 待复制列表.size();
    }



    @Override
    public int 测试用_删除复制的当前日期的底稿数据() {
        // 1. 当前日期
        String 当前日期 = DateTime.now().toString("yyyy-MM-dd");
        Date date = DateTime.parse(当前日期).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<ShuiJin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ShuiJin::getDay, date);
        List<ShuiJin> list = list(queryWrapper2);


        // 3. 删除
        for (ShuiJin one : list) {
            removeById(one.id);
        }

        return 1;
    }



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
        标题Cell.setCellValue("全产全销税金测算");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                税金项目HeaderCell = 表头行.createCell(0),
                税率HeaderCell = 表头行.createCell(1),
                备注HeaderCell = 表头行.createCell(2),
                编码HeaderCell = 表头行.createCell(3);
        税金项目HeaderCell.setCellValue("税金项目");
        税率HeaderCell.setCellValue("税率(万元)");
        备注HeaderCell.setCellValue("备注");
        编码HeaderCell.setCellValue("编码");
        表头行.forEach(cell -> cell.setCellStyle(表头样式));
        //#endregion


        //#region 3. 数据行
        List<PeiZhi> peiZhiList = peiZhiService.getByCategory(CATEGORY);
        int 行索引 = 2;
        for (PeiZhi peiZhi : peiZhiList) {
            HSSFRow row = sheet.createRow(行索引++);

            HSSFCell 税金项目Cell = row.createCell(0);
            税金项目Cell.setCellValue(peiZhi.name);

            HSSFCell 税率Cell = row.createCell(1);

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


        //#region 4. 设置合计的公式
        //#endregion


        // 设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 25 * 256);
        sheet.setColumnWidth(3, 0);


        // 为合并的单元格设置边框
        excelUtil.setBordersToMergedCells(sheet);


        return sheet;
    }



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
        final Date day = DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate();


        // 4. 定义数据行开始的位置
        int 数据行开始的位置 = 2;


        // 5. 获取数据
        int
                税金项目CellIndex = 0,
                税率CellIndex = 1,
                备注CellIndex = 2,
                编码CellIndex = 3;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<ShuiJin> fetchDataList = new ArrayList<>();
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 税金项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                ShuiJin one = new ShuiJin();
                one.day = day;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = CATEGORY;
                one.name = name.trim();
                one.税率 = excelUtil.getDouble(row, 税率CellIndex);
                one.remark = excelUtil.getString(row, 备注CellIndex);

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


        // 过滤空的单元格/行
        fetchDataList = fetchDataList.stream().filter(i -> i.税率 != null).collect(Collectors.toList());


        // 7. 待覆盖数据
        LambdaQueryWrapper<ShuiJin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShuiJin::getDay, day).orderByAsc(ShuiJin::getSort);
        List<ShuiJin> 待覆盖数据 = list(queryWrapper);
        for (ShuiJin one : 待覆盖数据) {
            ShuiJin found = fetchDataList.stream().filter(i -> i.code.equals(one.code)).findFirst().orElse(null);
            if (found == null) {
                continue;
            }
            one.税率 = found.税率;
            one.remark = found.remark;
        }


        // 8. 覆盖值
        for (ShuiJin one : 待覆盖数据) {
            gongShiService.修改单元格数据(CATEGORY, one.id, "税率", String.valueOf(one.税率));

            // 修改备注
            updateById(one);
        }


        //
        return 待覆盖数据.size();
    }




    private boolean insertOrUpdate(ShuiJin one) {
        ShuiJin dbOne = getUnique(one.getDayString(), one.code);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }




    private List<ShuiJin> _从Excel文件中提取数据(MultipartFile file) {

        // 1. 打开文件
        Workbook wb;
        wb = excelUtil.openExcel(file);
        if (wb == null) {
            return null;
        }


        // 2. 获取指定表格
        Sheet sheet = wb.getSheet(CATEGORY);


        // 3. 获取月份
        Date day = DateTime.parse("2020-09-01").toDate();


        // 4. 定义数据行开始的位置
        int 数据行开始的位置 = 3;


        // 5. 获取数据
        int
                税金项目CellIndex = 0,
                序号CellIndex = 1,
                税率CellIndex = 2,
                基数CellIndex = 3,
                应交税金CellIndex = 4,
                备注CellIndex = 5;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<ShuiJin> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 税金项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                ShuiJin one = new ShuiJin();
                one.day = day;
                one.code = PeiZhi.CATEGORY_税金及附加 + new DecimalFormat("000000").format(codeNumber++);
                one.category = CATEGORY;
                one.name = name.trim();
                one.xuhao = excelUtil.getInteger(row, 序号CellIndex);
                one.税率 = excelUtil.getDouble(row, 税率CellIndex);
                one.基数 = excelUtil.getDouble(row, 基数CellIndex);
                one.应交税金 = excelUtil.getDouble(row, 应交税金CellIndex);
                one.remark = excelUtil.getString(row, 备注CellIndex);
                one.sort = row.getRowNum();
                one.isBold = row.getCell(0).getCellStyle().getFontIndexAsInt() > 0;
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






}
