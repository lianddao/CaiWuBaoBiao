package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.YuanCaiLiaoMapper;
import com.hzsh.hzsh.model.YuanCaiLiao;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.model.YuanCaiLiao;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>原材料</h1>
 */
@Service
public class YuanCaiLiaoServiceImpl extends ServiceImpl<YuanCaiLiaoMapper, YuanCaiLiao> implements YuanCaiLiaoService {

    @Resource
    private ExcelUtil excelUtil;
    @Resource
    private PeiZhiService peiZhiService;
    @Resource
    private GongShiService gongShiService;

    private final String CATEGORY = "原材料表";




    /**
     * 从Excel导入数据
     */
    @Override
    public List<YuanCaiLiao> importYuanCaiLiao(MultipartFile file) {
        // 1. 从文件中提取数据
        List<YuanCaiLiao> list = _从Excel文件中提取数据(file);


        // 2. 新增或修改 [HZSH_PEI_ZHI]
        peiZhiService.autoInsertOrUpdate(list);


        // 3. 新增或修改到 [HZSH_YUAN_CAI_LIAO]
        String day = list.get(0).getDayString();
        for (YuanCaiLiao one : list) {
            insertOrUpdate(one);
        }


        return list;
    }



    /**
     * 指定日期的数据
     */
    @Override
    public List<YuanCaiLiao> defaultList(String day) {
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(YuanCaiLiao::getDay, DateTime.parse(day).toDate())
                .orderByAsc(YuanCaiLiao::getSort);
        List<YuanCaiLiao> list = baseMapper.selectList(queryWrapper);

        for (YuanCaiLiao one : list) {
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
    public YuanCaiLiao getUnique(String day, String code) {
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(YuanCaiLiao::getDay, DateTime.parse(day).toDate())
                .eq(YuanCaiLiao::getCode, code);
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
        QueryWrapper<YuanCaiLiao> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.select("MAX(DAY)").lambda().ne(YuanCaiLiao::getDay, DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toDate());
        Map<String, Object> map = getMap(queryWrapper1);
        Date date = new DateTime(map.get("MAX(DAY)")).toDate();


        // 2. 日期对应的数据
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(YuanCaiLiao::getDay, date);
        List<YuanCaiLiao> list = list(queryWrapper2);


        // 3. 复制
        List<YuanCaiLiao> 待复制列表 = new ArrayList<>();
        for (YuanCaiLiao one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            YuanCaiLiao newOne = JSON.parseObject(jsonObject.toJSONString(), YuanCaiLiao.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (YuanCaiLiao one : 待复制列表) {
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
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(YuanCaiLiao::getDay, date);
        List<YuanCaiLiao> list = list(queryWrapper2);


        // 3. 复制
        List<YuanCaiLiao> 待复制列表 = new ArrayList<>();
        for (YuanCaiLiao one : list) {
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(one));
            jsonObject.remove("id");
            jsonObject.put("day", 待保存日期);

            YuanCaiLiao newOne = JSON.parseObject(jsonObject.toJSONString(), YuanCaiLiao.class);
            待复制列表.add(newOne);
        }


        // 4. 保存到数据库
        for (YuanCaiLiao one : 待复制列表) {
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
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(YuanCaiLiao::getDay, date);
        List<YuanCaiLiao> list = list(queryWrapper2);


        // 3. 删除
        for (YuanCaiLiao one : list) {
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
        标题Cell.setCellValue("原油及外购原料油预算");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        标题Cell.setCellStyle(标题样式);
        //#endregion


        //#region 2. 表头行
        HSSFRow 表头行 = sheet.createRow(1);
        HSSFCell
                原油品种HeaderCell = 表头行.createCell(0),
                采购或加工数量HeaderCell = 表头行.createCell(1),
                编码HeaderCell = 表头行.createCell(2);
        原油品种HeaderCell.setCellValue("原油品种");
        采购或加工数量HeaderCell.setCellValue("采购或加工数量(吨)");
        编码HeaderCell.setCellValue("编码");
        表头行.forEach(cell -> cell.setCellStyle(表头样式));
        //#endregion


        //#region 3. 数据行
        List<PeiZhi> peiZhiList = peiZhiService.getByCategory(CATEGORY);
        int 行索引 = 2;
        List<HSSFRow> rowList = new ArrayList<>();

        for (PeiZhi peiZhi : peiZhiList) {
            HSSFRow row = sheet.createRow(行索引++);
            rowList.add(row);

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
            HSSFCell 原油品种Cell = row.createCell(0);
            原油品种Cell.setCellValue(name);

            // 输入列
            HSSFCell 采购或加工数量Cell = row.createCell(1);

            // 编码
            HSSFCell 编码Cell = row.createCell(2);
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


        //#region 4. 设置单元格的公式
        final int 输入列索引 = 1;
        final int 编码列索引 = 2;
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(GongShi::get表, CATEGORY)
                .eq(GongShi::get列, "本期采购数量吨")
                .orderByAsc(GongShi::getSort);
        List<GongShi> 公式List = gongShiService.list(queryWrapper);

        for (HSSFRow row : rowList) {
            String code = row.getCell(编码列索引).getStringCellValue();
            GongShi 公式 = 公式List.stream().filter(i -> i.code.equals(code)).findFirst().orElse(null);

            if (公式 == null) {
                continue;
            }

            // 设置行加粗
            row.forEach(cell -> cell.setCellStyle(数据行加粗样式));


            List<String> codeList = Arrays.asList(公式.公式的项目完整体.split(",")).stream().map(i -> i.split("#")[1]).collect(Collectors.toList());
            String 地址公式;
            if (codeList.size() > 2) {
                String 开始编码 = codeList.get(0);
                String 结束编码 = codeList.get(codeList.size() - 1);
                地址公式 = rowList.stream()
                        .filter(i -> i.getCell(编码列索引).getStringCellValue().equals(开始编码) || i.getCell(编码列索引).getStringCellValue().equals(结束编码))
                        .map(j -> j.getCell(输入列索引).getAddress().toString())
                        .collect(Collectors.joining(":"));
                地址公式 = "SUM(" + 地址公式 + ")";
                row.getCell(输入列索引).setCellFormula(地址公式);
            }
            else if (codeList.size() == 2) {
                地址公式 = rowList.stream()
                        .filter(i -> codeList.contains(i.getCell(编码列索引).getStringCellValue()))
                        .map(j -> j.getCell(输入列索引).getAddress().toString())
                        .collect(Collectors.joining("+"));
                row.getCell(输入列索引).setCellFormula(地址公式);
            }
            else {
                地址公式 = null;
            }

            System.out.println(地址公式);
        }
        //#endregion


        // 设置列的宽度
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 0);


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
                采购或加工数量CellIndex = 1,
                编码CellIndex = 2;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<YuanCaiLiao> fetchDataList = new ArrayList<>();
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
                YuanCaiLiao one = new YuanCaiLiao();
                one.day = day;
                one.code = excelUtil.getString(row, 编码CellIndex);
                one.category = CATEGORY;
                one.name = name.trim();
                one.本期采购数量吨 = excelUtil.getDouble(row, 采购或加工数量CellIndex);

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
        fetchDataList = fetchDataList.stream().filter(i -> i.本期采购数量吨 != null).collect(Collectors.toList());


        // 7. 待覆盖数据
        LambdaQueryWrapper<YuanCaiLiao> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(YuanCaiLiao::getDay, day).orderByAsc(YuanCaiLiao::getSort);
        List<YuanCaiLiao> 待覆盖数据 = list(queryWrapper);
        for (YuanCaiLiao one : 待覆盖数据) {
            YuanCaiLiao found = fetchDataList.stream().filter(i -> i.code.equals(one.code)).findFirst().orElse(null);
            if (found == null) {
                continue;
            }
            one.本期采购数量吨 = found.本期采购数量吨;
        }


        // 8. 覆盖值
        for (YuanCaiLiao one : 待覆盖数据) {
            gongShiService.修改单元格数据(CATEGORY, one.id, "本期采购数量吨", String.valueOf(one.本期采购数量吨));
        }


        //
        return 待覆盖数据.size();
    }






    private boolean insertOrUpdate(YuanCaiLiao one) {
        YuanCaiLiao dbOne = getUnique(one.getDayString(), one.code);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }




    private List<YuanCaiLiao> _从Excel文件中提取数据(MultipartFile file) {

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
        int 数据行开始的位置 = 6;


        // 5. 获取数据
        int
                SAP物料号CellIndex = 1,
                原油品种CellIndex = 2,
                本期采购_数量_吨CellIndex = 6,
                本期采购_数量_桶CellIndex = 7,
                本期采购_价格_小结CellIndex = 8,
                本期采购_价格_结算价格_美元CellIndex = 9,
                本期采购_价格_结算价格_元CellIndex = 10,
                本期采购_价格_单位运杂费CellIndex = 11,
                本期采购_到厂运杂费CellIndex = 12,
                本期采购_到厂总成本CellIndex = 13,
                本期加工_数量CellIndex = 14,
                本期加工_单位成本CellIndex = 15,
                本期加工_总成本CellIndex = 16,
                吨桶比CellIndex = 21;
        int currentRowNum = 0;  // 如果发生异常时,保存异常行位置
        List<YuanCaiLiao> fetchDataList = new ArrayList<>();

        int codeNumber = 1;
        try {
            for (Row row : sheet) {
                if (row.getRowNum() < 数据行开始的位置) {
                    continue;   // 跳过无意义数据
                }
                if (row.getFirstCellNum() == - 1) {
                    break;  // 获取结束
                }
                String name = excelUtil.getString(row, 原油品种CellIndex);
                if (StringUtils.isEmpty(name)) {
                    continue; // 跳过空名称的数据
                }
                currentRowNum = row.getRowNum();


                // 1. 基础数据
                YuanCaiLiao one = new YuanCaiLiao();
                one.day = day;
                one.code = PeiZhi.CATEGORY_原材料表 + new DecimalFormat("000000").format(codeNumber++);
                one.category = CATEGORY;
                one.name = name.trim();
                one.sapCode = excelUtil.getInteger(row, SAP物料号CellIndex);

                one.本期采购数量吨 = excelUtil.getDouble(row, 本期采购_数量_吨CellIndex);
                one.本期采购数量桶 = excelUtil.getDouble(row, 本期采购_数量_桶CellIndex);
                one.本期采购价格小计 = excelUtil.getDouble(row, 本期采购_价格_小结CellIndex);
                one.本期采购价格美元 = excelUtil.getDouble(row, 本期采购_价格_结算价格_美元CellIndex);
                one.本期采购价格元 = excelUtil.getDouble(row, 本期采购_价格_结算价格_元CellIndex);
                one.本期采购单位运杂费 = excelUtil.getDouble(row, 本期采购_价格_单位运杂费CellIndex);
                one.本期采购到厂运杂费 = excelUtil.getDouble(row, 本期采购_到厂运杂费CellIndex);
                one.本期采购到厂总成本 = excelUtil.getDouble(row, 本期采购_到厂总成本CellIndex);

                one.本期加工数量 = excelUtil.getDouble(row, 本期加工_数量CellIndex);
                one.本期加工单位成本 = excelUtil.getDouble(row, 本期加工_单位成本CellIndex);
                one.本期加工总成本 = excelUtil.getDouble(row, 本期加工_总成本CellIndex);

                //                one.吨桶比 = excelUtil.getDouble(row, 吨桶比CellIndex);
                one.sort = row.getRowNum();
                one.isBold = row.getCell(2).getCellStyle().getFontIndexAsInt() > 0;
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
