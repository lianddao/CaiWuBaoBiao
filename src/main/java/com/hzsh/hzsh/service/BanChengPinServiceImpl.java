package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.BanChengPinMapper;
import com.hzsh.hzsh.model.BanChengPin;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.util.ExcelUtil;
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
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 半成品 - [HZSH_BAN_CHENG_PIN]
 */
@Service
public class BanChengPinServiceImpl extends ServiceImpl<BanChengPinMapper, BanChengPin> implements BanChengPinService {


    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    @Resource
    private GongShiService gongShiService;

    private final String CATEGORY = "半成品";



    /**
     * 从Excel导入数据
     */
    @Override
    public List<BanChengPin> importBanChengPin(MultipartFile file) {
        // 1. 从文件中提取数据
        List<BanChengPin> list = _从Excel文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [HZSH_BAN_CHENG_PIN]
        String day = list.get(0).getDayString();
        for (BanChengPin one : list) {
            insertOrUpdate(one);
        }


        return list;
    }


    /**
     * 指定日期的数据
     */
    @Override
    public List<BanChengPin> defaultList(String day) {
        LambdaQueryWrapper<BanChengPin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(BanChengPin::getDay, DateTime.parse(day).toDate())
                .orderByAsc(BanChengPin::getSort);
        List<BanChengPin> list = baseMapper.selectList(queryWrapper);

        for (BanChengPin one : list) {
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
    public BanChengPin getUnique(String day, String code) {
        LambdaQueryWrapper<BanChengPin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(BanChengPin::getDay, DateTime.parse(day).toDate())
                .eq(BanChengPin::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }



    /**
     * 修改
     *
     * @param name
     * @param value
     * @param pk
     */
    @Override
    public int edit(String name, String value, Long pk) {
        gongShiService.修改单元格数据(CATEGORY, pk, name, value);
        return 1;
    }






    @Override
    public int 复制最近日期的数据(String 待保存日期) {
        // 1. 最近有数据的日期
        QueryWrapper<BanChengPin> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("MAX(DAY)").lambda().ne(BanChengPin::getDay, DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate());
        Map<String, Object> map = getMap(queryWrapper1);
        Date date = new DateTime(map.get("MAX(DAY)")).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<BanChengPin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BanChengPin::getDay, date);
        List<BanChengPin> list = list(queryWrapper2);


        // 3. 复制
        List<BanChengPin> 待复制列表 = new ArrayList<>();
        for (BanChengPin one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            BanChengPin newOne = JSON.parseObject(jsonObject.toJSONString(), BanChengPin.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (BanChengPin one : 待复制列表) {
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
        LambdaQueryWrapper<BanChengPin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BanChengPin::getDay, date);
        List<BanChengPin> list = list(queryWrapper2);


        // 3. 复制
        List<BanChengPin> 待复制列表 = new ArrayList<>();
        for (BanChengPin one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            BanChengPin newOne = JSON.parseObject(jsonObject.toJSONString(), BanChengPin.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (BanChengPin one : 待复制列表) {
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
        LambdaQueryWrapper<BanChengPin> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BanChengPin::getDay, date);
        List<BanChengPin> list = list(queryWrapper2);


        // 3. 删除
        for (BanChengPin one : list) {
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
        标题Cell.setCellValue("半成品预算");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                项目HeaderCell = 表头行.createCell(0),
                对应产成品HeaderCell = 表头行.createCell(1),
                库存数量HeaderCell = 表头行.createCell(2),
                编码HeaderCell = 表头行.createCell(3);
        项目HeaderCell.setCellValue("项目");
        对应产成品HeaderCell.setCellValue("对应产成品");
        库存数量HeaderCell.setCellValue("库存数量(万吨)");
        编码HeaderCell.setCellValue("编码");
        表头行.forEach(cell -> cell.setCellStyle(表头样式));
        //#endregion


        //#region 3. 数据行
        // 根据底稿数据,获取「对应产成品」列值
        final Date 底稿日期 = DateTime.parse("2020-09-01").toDate();
        LambdaQueryWrapper<BanChengPin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BanChengPin::getDay, 底稿日期).orderByAsc(BanChengPin::getSort);
        List<BanChengPin> 底稿数据 = list(queryWrapper);

        List<PeiZhi> peiZhiList = peiZhiService.getByCategory(CATEGORY);
        int 行索引 = 2;
        for (PeiZhi peiZhi : peiZhiList) {
            HSSFRow row = sheet.createRow(行索引++);

            HSSFCell 项目Cell = row.createCell(0);
            项目Cell.setCellValue(peiZhi.name);

            BanChengPin found = 底稿数据.stream().filter(i -> i.code.equals(peiZhi.code)).findFirst().orElse(null);
            HSSFCell 对应产成品Cell = row.createCell(1);
            对应产成品Cell.setCellValue(found == null ? "" : found.chengPinName);

            HSSFCell 库存数量Cell = row.createCell(2);

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
        HSSFRow 合计行 = sheet.getRow(2);
        合计行.forEach(cell -> cell.setCellStyle(数据行加粗样式));
        HSSFCell 合计行_库存数量列 = 合计行.getCell(2);
        合计行_库存数量列.setCellFormula(
                String.format("SUM(%s:%s)",
                        sheet.getRow(3).getCell(2).getAddress().toString(),
                        sheet.getRow(sheet.getLastRowNum()).getCell(2).getAddress().toString()
                )
        );
        //#endregion


        // 设置列的宽度
        sheet.setColumnWidth(0, 55 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 15 * 256);
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
                项目CellIndex = 0,
                库存数量CellIndex = 2,
                编码CellIndex = 3;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<BanChengPin> fetchDataList = new ArrayList<>();
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
                BanChengPin one = new BanChengPin();
                one.day = day;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = CATEGORY;
                one.name = name.trim();
                one.期初库存数量 = excelUtil.getDouble(row, 库存数量CellIndex);

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
        fetchDataList = fetchDataList.stream().filter(i -> i.期初库存数量 != null).collect(Collectors.toList());


        // 7. 待覆盖数据
        LambdaQueryWrapper<BanChengPin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BanChengPin::getDay, day).orderByAsc(BanChengPin::getSort);
        List<BanChengPin> 待覆盖数据 = list(queryWrapper);
        for (BanChengPin one : 待覆盖数据) {
            BanChengPin found = fetchDataList.stream().filter(i -> i.code.equals(one.code)).findFirst().orElse(null);
            if (found == null) {
                continue;
            }
            one.期初库存数量 = found.期初库存数量;
        }


        // 8. 覆盖值
        for (BanChengPin one : 待覆盖数据) {
            gongShiService.修改单元格数据(CATEGORY, one.id, "期初库存数量", String.valueOf(one.期初库存数量));
        }


        //
        return 待覆盖数据.size();
    }





    private boolean insertOrUpdate(BanChengPin one) {
        BanChengPin dbOne = getUnique(one.getDayString(), one.code);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }




    private List<BanChengPin> _从Excel文件中提取数据(MultipartFile file) {

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
        int 数据行开始的位置 = 4;


        // 5. 获取数据
        int
                半成品项目CellIndex = 0,
                对应产成品CellIndex = 1,
                期初库存_数量CellIndex = 2,
                期初库存_单价CellIndex = 3,
                期初库存_金额CellIndex = 4,
                期末库存_数量CellIndex = 5,
                期末库存_单价CellIndex = 6,
                期末库存_金额CellIndex = 7,
                库存减少数量CellIndex = 8;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<BanChengPin> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 半成品项目CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                BanChengPin one = new BanChengPin();
                one.day = day;
                one.code = PeiZhi.CATEGORY_半成品 + new DecimalFormat("000000").format(codeNumber++);
                one.category = CATEGORY;
                one.name = name.trim();
                one.chengPinName = excelUtil.getString(row, 对应产成品CellIndex);

                one.期初库存数量 = excelUtil.getDouble(row, 期初库存_数量CellIndex);
                one.期初库存单价 = excelUtil.getDouble(row, 期初库存_单价CellIndex);
                one.期初库存金额 = excelUtil.getDouble(row, 期初库存_金额CellIndex);

                one.期末库存数量 = excelUtil.getDouble(row, 期末库存_数量CellIndex);
                one.期末库存单价 = excelUtil.getDouble(row, 期末库存_单价CellIndex);
                one.期末库存金额 = excelUtil.getDouble(row, 期末库存_金额CellIndex);

                one.库存减少数量 = excelUtil.getDouble(row, 库存减少数量CellIndex);
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
