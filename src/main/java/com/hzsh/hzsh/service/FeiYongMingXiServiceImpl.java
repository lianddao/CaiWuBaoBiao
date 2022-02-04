package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.FeiYongMingXiMapper;
import com.hzsh.hzsh.model.BanChengPin;
import com.hzsh.hzsh.model.FeiYongMingXi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
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
 * 费用明细 - [HZSH_FEI_YONG]
 */
@Service
public class FeiYongMingXiServiceImpl extends ServiceImpl<FeiYongMingXiMapper, FeiYongMingXi> implements FeiYongMingXiService {

    @Resource
    private ExcelUtil excelUtil;

    @Resource
    private PeiZhiService peiZhiService;

    @Resource
    private GongShiService gongShiService;

    private final String CATEGORY = "费用明细";



    /**
     * 从Excel底稿导入数据
     */
    @Override
    public List<FeiYongMingXi> importFeiYong(MultipartFile file) {
        // 1. 从文件中提取数据
        List<FeiYongMingXi> list = _从Excel底稿文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [HZSH_FEI_YONG]
        String day = list.get(0).getDayString();
        for (FeiYongMingXi one : list) {
            insertOrUpdate(one);
        }


        return list;
    }



    /**
     * 指定日期的数据
     */
    @Override
    public List<FeiYongMingXi> defaultList(String day) {
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(FeiYongMingXi::getDay, DateTime.parse(day).toDate())
                .orderByAsc(FeiYongMingXi::getSort);
        List<FeiYongMingXi> list = baseMapper.selectList(queryWrapper);

        for (FeiYongMingXi one : list) {
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
    public FeiYongMingXi getUnique(String day, String code) {
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(FeiYongMingXi::getDay, DateTime.parse(day).toDate())
                .eq(FeiYongMingXi::getCode, code);
        return baseMapper.selectOne(queryWrapper);
    }



    /**
     * 修改
     */
    @Override
    public int edit(String name, String value, Long pk) {
        gongShiService.修改单元格数据(CATEGORY, pk, name, value);
        return 1;
    }





    @Override
    public int 复制最近日期的数据(String 待保存日期) {
        // 1. 最近有数据的日期
        QueryWrapper<FeiYongMingXi> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("MAX(DAY)").lambda().ne(FeiYongMingXi::getDay, DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate());
        Map<String, Object> map = getMap(queryWrapper1);
        Date date = new DateTime(map.get("MAX(DAY)")).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(FeiYongMingXi::getDay, date);
        List<FeiYongMingXi> list = list(queryWrapper2);


        // 3. 复制
        List<FeiYongMingXi> 待复制列表 = new ArrayList<>();
        for (FeiYongMingXi one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            FeiYongMingXi newOne = JSON.parseObject(jsonObject.toJSONString(), FeiYongMingXi.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (FeiYongMingXi one : 待复制列表) {
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
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(FeiYongMingXi::getDay, date);
        List<FeiYongMingXi> list = list(queryWrapper2);


        // 3. 复制
        List<FeiYongMingXi> 待复制列表 = new ArrayList<>();
        for (FeiYongMingXi one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            FeiYongMingXi newOne = JSON.parseObject(jsonObject.toJSONString(), FeiYongMingXi.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (FeiYongMingXi one : 待复制列表) {
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
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(FeiYongMingXi::getDay, date);
        List<FeiYongMingXi> list = list(queryWrapper2);


        // 3. 删除
        for (FeiYongMingXi one : list) {
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
        标题Cell.setCellValue("费用预算");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                项目HeaderCell = 表头行.createCell(0),
                合计HeaderCell = 表头行.createCell(1),
                生产费用HeaderCell = 表头行.createCell(2),
                管理费用HeaderCell = 表头行.createCell(3),
                销售费用HeaderCell = 表头行.createCell(4),
                备注HeaderCell = 表头行.createCell(5),
                编码HeaderCell = 表头行.createCell(6);
        项目HeaderCell.setCellValue("项目");
        合计HeaderCell.setCellValue("合计");
        生产费用HeaderCell.setCellValue("生产费用");
        管理费用HeaderCell.setCellValue("管理费用");
        销售费用HeaderCell.setCellValue("销售费用");
        备注HeaderCell.setCellValue("备注");
        编码HeaderCell.setCellValue("编码");
        表头行.forEach(cell -> cell.setCellStyle(表头样式));
        //#endregion


        //#region 3. 数据行
        List<PeiZhi> peiZhiList = peiZhiService.getByCategory(CATEGORY);
        int 行索引 = 2;
        for (PeiZhi peiZhi : peiZhiList) {
            HSSFRow row = sheet.createRow(行索引++);

            HSSFCell 项目Cell = row.createCell(0);
            项目Cell.setCellValue(peiZhi.name);

            HSSFCell 合计Cell = row.createCell(1);
            合计Cell.setCellType(CellType.FORMULA);

            HSSFCell 生产费用Cell = row.createCell(2);
            HSSFCell 管理费用Cell = row.createCell(3);
            HSSFCell 销售费用Cell = row.createCell(4);

            // 设置'合计Cell'的公式
            合计Cell.setCellFormula(String.format("%s+%s+%s",
                    生产费用Cell.getAddress().toString(),
                    管理费用Cell.getAddress().toString(),
                    销售费用Cell.getAddress().toString()
            ));


            HSSFCell 备注Cell = row.createCell(5);
            备注Cell.setCellType(CellType.STRING);

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


        //#region 4. 设置合计的公式
        HSSFRow 合计行 = sheet.getRow(2);
        合计行.forEach(cell -> cell.setCellStyle(数据行加粗样式));
        HSSFCell
                合计行_合计列 = 合计行.getCell(1),
                合计行_生产费用列 = 合计行.getCell(2),
                合计行_管理费用列 = 合计行.getCell(3),
                合计行_销售费用列 = 合计行.getCell(4);

        合计行_合计列.setCellFormula(
                String.format("SUM(%s:%s)",
                        sheet.getRow(3).getCell(1).getAddress().toString(),
                        sheet.getRow(sheet.getLastRowNum()).getCell(1).getAddress().toString()
                )
        );

        合计行_生产费用列.setCellFormula(
                String.format("SUM(%s:%s)",
                        sheet.getRow(3).getCell(2).getAddress().toString(),
                        sheet.getRow(sheet.getLastRowNum()).getCell(2).getAddress().toString()
                )
        );

        合计行_管理费用列.setCellFormula(
                String.format("SUM(%s:%s)",
                        sheet.getRow(3).getCell(3).getAddress().toString(),
                        sheet.getRow(sheet.getLastRowNum()).getCell(3).getAddress().toString()
                )
        );

        合计行_销售费用列.setCellFormula(
                String.format("SUM(%s:%s)",
                        sheet.getRow(3).getCell(4).getAddress().toString(),
                        sheet.getRow(sheet.getLastRowNum()).getCell(4).getAddress().toString()
                )
        );
        //#endregion




        // 设置列的宽度
        sheet.setColumnWidth(0, 25 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 25 * 256);
        sheet.setColumnWidth(6, 0);


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
                生产费用CellIndex = 2,
                管理费用CellIndex = 3,
                销售费用CellIndex = 4,
                备注CellIndex = 5,
                编码CellIndex = 6;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<FeiYongMingXi> fetchDataList = new ArrayList<>();
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
                FeiYongMingXi one = new FeiYongMingXi();
                one.day = day;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = CATEGORY;
                one.name = name.trim();
                one.生产费用 = excelUtil.getDouble(row, 生产费用CellIndex);
                one.管理费用 = excelUtil.getDouble(row, 管理费用CellIndex);
                one.销售费用 = excelUtil.getDouble(row, 销售费用CellIndex);
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
        fetchDataList = fetchDataList.stream().filter(i -> ! (i.生产费用 == null && i.管理费用 == null && i.销售费用 == null)).collect(Collectors.toList());


        // 7. 待覆盖数据
        LambdaQueryWrapper<FeiYongMingXi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FeiYongMingXi::getDay, day).orderByAsc(FeiYongMingXi::getSort);
        List<FeiYongMingXi> 待覆盖数据 = list(queryWrapper);
        for (FeiYongMingXi one : 待覆盖数据) {
            FeiYongMingXi found = fetchDataList.stream().filter(i -> i.code.equals(one.code)).findFirst().orElse(null);
            if (found == null) {
                continue;
            }
            one.生产费用 = found.生产费用;
            one.管理费用 = found.管理费用;
            one.销售费用 = found.销售费用;
            one.remark = found.remark;
        }


        // 8. 覆盖值
        for (FeiYongMingXi one : 待覆盖数据) {
            gongShiService.修改单元格数据(CATEGORY, one.id, "生产费用", String.valueOf(one.生产费用));
            gongShiService.修改单元格数据(CATEGORY, one.id, "管理费用", String.valueOf(one.管理费用));
            gongShiService.修改单元格数据(CATEGORY, one.id, "销售费用", String.valueOf(one.销售费用));


            // 修改备注
            updateById(one);
        }


        //
        return 待覆盖数据.size();
    }






    private boolean insertOrUpdate(FeiYongMingXi one) {
        FeiYongMingXi dbOne = getUnique(one.getDayString(), one.code);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }






    private List<FeiYongMingXi> _从Excel底稿文件中提取数据(MultipartFile file) {

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
                项目CellIndex = 0,
                合计CellIndex = 2,
                生产费用CellIndex = 3,
                管理费用CellIndex = 4,
                销售费用CellIndex = 5,
                备注CellIndex = 6;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<FeiYongMingXi> fetchDataList = new ArrayList<>();


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
                FeiYongMingXi one = new FeiYongMingXi();
                one.day = day;
                one.code = PeiZhi.CATEGORY_费用明细 + new DecimalFormat("000000").format(codeNumber++);
                one.category = CATEGORY;
                one.name = name.trim();
                one.合计 = excelUtil.getDouble(row, 合计CellIndex);
                one.生产费用 = excelUtil.getDouble(row, 生产费用CellIndex);
                one.管理费用 = excelUtil.getDouble(row, 管理费用CellIndex);
                one.销售费用 = excelUtil.getDouble(row, 销售费用CellIndex);
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
