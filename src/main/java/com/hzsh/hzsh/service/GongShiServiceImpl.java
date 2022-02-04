package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.common.utils.MyException;
import com.hzsh.hzsh.controller.GongShiCache;
import com.hzsh.hzsh.dto.CodeAndValue;
import com.hzsh.hzsh.dto.GongShiDTO;
import com.hzsh.hzsh.mapper.GongShiMapper;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.JiaGe;
import com.hzsh.hzsh.model.JiaGeLiShi;
import com.hzsh.hzsh.model.PeiZhi;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * 公式表 [HZSH_GONG_SHI]
 */
@Service
public class GongShiServiceImpl extends ServiceImpl<GongShiMapper, GongShi> implements GongShiService {


    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PeiZhiService peiZhiService;
    @Resource
    private GongShiService gongShiService;
    @Resource
    private GongShiMapper gongShiMapper;
    @Resource
    private JiaGeService jiaGeService;
    @Resource
    private JiaGeLiShiService jiaGeLiShiService;



    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine engine = manager.getEngineByName("js");



    private final List<String> _运算符 = Arrays.asList("+", "-", "*", "/", "(", ")"); // 不能有冒号值

    private final List<String> _EXCEL公式 = Arrays.asList("IF", "SUM", "SUMIF", "SUMPRODUCT");    // 不能有冒号值




    /**
     * 更新对象本身
     */
    @Override
    public int edit(String name, String value, Long pk) {
        if (StringUtils.isEmpty(value)) {
            // ①. 若设置空值, 数字类型的值需要这里特别处理. (字符串类型也适用)
            Field field = Arrays.stream(GongShi.class.getDeclaredFields()).filter(i -> i.getName().equals(name)).findFirst().get();
            TableField tableField = field.getAnnotation(TableField.class);
            String 数据库列名 = tableField == null ? name : tableField.value();
            UpdateWrapper<GongShi> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", pk).set(数据库列名, value);
            update(updateWrapper);
        }
        else {
            // ②. 非空值的修改
            Map map = new HashMap();
            map.put(name, value);
            GongShi one = JSON.parseObject(JSON.toJSONString(map), GongShi.class);
            one.id = getById(pk).id;
            updateById(one);
        }

        return 1;
    }






    /**
     * 获取项目对应的公式列表
     *
     * @param code
     * @return
     */
    @Override
    public List<GongShi> getGongShiList(String code) {
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GongShi::getCode, code).orderByAsc(GongShi::getSort);
        List<GongShi> list = gongShiService.list(queryWrapper);

        return list;
    }






    @Override
    public Object 计算(Object 公式id, String gong_shi, String day) {
        gong_shi = gong_shi.trim();



        List<String> strings = Arrays.asList(gong_shi.split("\\s+"));






        //#region 1. 逐个计算Excel公式
        if (strings.get(0).equals("IF")) {
            Object v1 = IF(公式id, gong_shi, day);
            System.out.print(gong_shi);
            System.out.println(" = " + v1);
            return v1;
        }


        if (strings.get(0).equals("SUM")) {
            Object v1 = SUM(公式id, gong_shi, day);
            System.out.print(gong_shi);
            System.out.println(" = " + v1);
            return v1;
        }


        if (strings.get(0).equals("SUMIF")) {
            Object v1 = SUMIF(公式id, gong_shi, day);
            System.out.print(gong_shi);
            System.out.println(" = " + v1);
            return v1;
        }


        if (strings.get(0).equals("SUMPRODUCT")) {
            Object v1 = SUMPRODUCT(公式id, gong_shi, day);
            System.out.print(gong_shi);
            System.out.println(v1);
            return v1;
        }
        //#endregion






        //#region 2. 逐个计算 + - * /
        String 数字公式 = "";
        int aaa = 1;
        int index = 0;
        for (String item : strings) {
            Object 值 = "";
            if (! _运算符.contains(item)) {
                String[] vs = item.split("#");

                // ① 若包含价格表公式, 且测试日期为 2020-09-01, 使用底稿测试值
                if (vs.length == 5 && day.equals("2020-09-01")) {
                    值 = vs[4];
                }

                // ②
                else if (vs[0].startsWith("价格表")) {

                    String rowCode = vs[1];
                    String colName = vs[3];

                    Object v1 = null;
                    // 底稿日期, 当天日期, 是为测试
                    if ("2020-09-01".equals(day) || DateTime.now().toString("yyyy-MM-dd").equals(day)) {
                        // TODO 测试底稿用, 定时任务则跳过
                        //#region 测试底稿用
                        if (rowCode.equals("1001000026")) {
                            // 布伦特
                            switch (colName) {
                                case "裸税价":
                                    v1 = 40.87;
                                    break;
                                case "汇率":
                                    v1 = 6.84;
                                    break;
                            }
                        }
                        else if (rowCode.equals("1001000027")) {
                            // 迪拜
                            switch (colName) {
                                case "裸税价":
                                    v1 = 41.99;
                                    break;
                            }
                        }
                        else if (rowCode.equals("1001000024")) {
                            // OmanDubai
                            switch (colName) {
                                case "裸税价":
                                    v1 = 42.05;
                                    break;
                            }
                        }
                        else if (rowCode.equals("1001000028")) {
                            // Oman官价
                            switch (colName) {
                                case "裸税价":
                                    v1 = 42.11;
                                    break;
                            }
                        }
                        else {
                            // 其他种类的价格
                            GongShi 价格公式 = getById((Serializable) 公式id);
                            String 测试用公式 = 价格公式.测试用公式;
                            String[] 测试用公式元素 = 测试用公式.split("#");
                            //                            v1 = 测试用公式元素[4];
                            v1 = 测试用公式元素[4].split("\\s+")[0];
                            System.out.println();

                        }
                        //#endregion
                    }
                    else {
                        //#region 实际数据的'价格表'公式值的获取
                        JiaGe jiaGe = jiaGeService.getLast(rowCode);
                        if (jiaGe != null) {
                            // ① 设置值
                            switch (colName) {
                                case "含税价":
                                    v1 = jiaGe.含税价;
                                    break;
                                case "裸税价":
                                    v1 = jiaGe.裸税价;
                                    break;
                                case "汇率":
                                    v1 = jiaGe.汇率;
                                    break;
                            }


                            // ② 转换为'JiaGeLiShi'对象
                            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(jiaGe));
                            jsonObject.remove("id");
                            JiaGeLiShi jiaGeLiShi = jsonObject.toJavaObject(JiaGeLiShi.class);


                            // ③ 保存到 [价格历史数据] 表
                            jiaGeLiShi.对应的单元格公式 = item;
                            jiaGeLiShiService.insertOrUpdate(jiaGeLiShi);
                        }
                        //#endregion
                    }

                    System.out.println(strings.get(0) + " = " + v1);
                    值 = v1 == null ? 0 : v1;
                }

                // ③ 没有代入值的公式
                else if (vs.length == 3) {
                    String 表名 = vs[0];
                    String 行名 = vs[1];
                    String 列名 = vs[2];

                    List<GongShiDTO> gongShiDTOList = GongShiCache.getTableList();
                    GongShiDTO gongShiDTO = gongShiDTOList.stream().filter(i -> i.biaoMing.equals(表名)).findFirst().get();
                    String tableName = gongShiDTO.tableName;
                    String code = 行名;

                    String columnName = null;
                    try {
                        columnName = gongShiDTO.columns.stream().filter(i -> i.lieMing.equals(列名)).findFirst().get().colName;
                    } catch (NoSuchElementException noSuchElementException) {
                        noSuchElementException.printStackTrace();
                        System.out.println("公式的格式可能不正确");
                    }


                    // 查询配置表,如果存在公式,则先计算公式
                    Object value;
                    String 取值公式;



                    // 加快「受触发公式」的获取速度
                    //                    System.out.println();
                    System.out.print(String.format("❁从 GongShiCache 中取出「%s %s %s」的公式", 表名, 行名, 列名));
                    String finalColumnName = columnName;
                    Optional<GongShi> optional = GongShiCache.获取所有公式数据().stream().filter(i -> i.code.equals(code) && i.列.equals(finalColumnName)).findFirst();
                    Long 嵌套调用中的测试用公式id = null;
                    if (optional.isPresent()) {
                        取值公式 = optional.get().单元格公式;
                        System.out.println("  →  " + 取值公式);
                        嵌套调用中的测试用公式id = optional.get().id;
                    }
                    else {
                        取值公式 = null;
                        System.out.println("  →  无取值公式");
                    }



                    if (! StringUtils.isEmpty(取值公式)) {
                        value = 计算(嵌套调用中的测试用公式id, 取值公式, day);
                        //                        System.out.println(表名 + "." + 行名 + "." + 列名 + " = " + 取值公式 + " = " + value);
                    }
                    else {
                        System.out.print(String.format("(%d) ", (aaa++)) + item + "  →  ");
                        String 查询语句 = String.format("SELECT \"%s\" FROM %s WHERE CODE=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, code, day);

                        try {
                            value = jdbcTemplate.queryForObject(查询语句, Double.class);
                        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                            value = null;
                        }

                        value = value == null ? 0 : value;
                        System.out.println(查询语句 + " = " + value);
                        System.out.println();

                    }
                    值 = String.valueOf(value);

                }

                // ④ 已代入值的公式
                else if (vs.length == 4) {
                    值 = vs[3];
                }

                // ⑤
                else if (_EXCEL公式.contains(item)) {
                    String 剩余公式体字符串 = strings.subList(index, strings.size()).stream().map(String::toString).collect(Collectors.joining(" "));
                    Object v1 = 计算(公式id, 剩余公式体字符串, day);
                    数字公式 += " " + v1;
                    break;
                }

                // ⑤
                else {
                    数字公式 += " " + item;
                }
            }
            else {
                数字公式 += " " + item;
            }

            数字公式 += " " + 值;
            index++;
        }
        //#endregion




        // 3. Eval
        try {
            // 替换一些不能识别的运算符: 如 --5200
            数字公式 = 数字公式
                    .replaceAll("--", "+")
                    .replaceAll("=", "==")
                    .replaceAll("&", "&&")
                    .replaceAll("\\|", "\\|\\|");

            Object result = engine.eval(数字公式);
            if (result != null && result.toString().equals("NaN")) {
                result = 0;
            }

            if (result instanceof Boolean) {
                System.out.println(数字公式 + " = " + result);
                return result;
            }
            else {
                if (result == null) {
                    // TODO
                    System.out.println(数字公式 + " = (null)");
                    return null;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("适应Integer和Double类型", result);
                Double x = Double.valueOf(map.get("适应Integer和Double类型").toString());

                if (x == Double.NaN) {
                    System.out.println(数字公式 + " = (NaN)");
                    return null;
                }

                System.out.println(数字公式 + " = " + x);
                return x;
            }
        } catch (ScriptException e) {
            e.printStackTrace();
            return null;
        }

    }






    @Override
    public Object IF(Object 公式id, String IF的公式, String day) {
        List<String> strings = Arrays.asList(IF的公式.trim().split("\\s+"));
        int 逗号的位置 = strings.indexOf(",");
        String 第一部分 = strings.subList(2, 逗号的位置).stream().map(String::toString).collect(Collectors.joining(" "));
        System.out.println(第一部分);
        Object v1 = 计算(公式id, 第一部分, day);


        List<String> 剩余部分 = strings.subList(逗号的位置 + 1, strings.size());
        逗号的位置 = 剩余部分.indexOf(",");
        String 第二部分 = 剩余部分.subList(0, 逗号的位置).stream().map(String::toString).collect(Collectors.joining(" "));
        System.out.println(第二部分);
        Object v2 = 计算(公式id, 第二部分, day);


        // 若第一部分判断结果为真
        if (v1.equals(true)) {
            System.out.println(v2);
            return v2;
        }


        //
        剩余部分 = 剩余部分.subList(逗号的位置 + 1, 剩余部分.size() - 1); // ★ 注意,使用 剩余部分.size() - 1 是正确的
        String 第三部分 = 剩余部分.subList(0, 剩余部分.size()).stream().map(String::toString).collect(Collectors.joining(" "));
        System.out.println(第三部分);
        Object v3 = 计算(公式id, 第三部分, day);

        System.out.println(v3);
        return v3;
    }




    @Override
    public Object SUM(Object 公式id, String SUM的公式, String day) {
        List<String> strings = Arrays.asList(SUM的公式.trim().split("\\s+"));
        List<String> strings辅助 = Arrays.asList(SUM的公式.trim().split("\\s+"));

        // 1. 存在缩写语法
        if (strings.contains(":")) {
            List<String> 缩写语法段 = new ArrayList<>();
            int length = strings.size();
            IntStream.range(0, length)
                    .filter(index -> strings.get(index).contains(":"))
                    .forEach(j -> {
                        缩写语法段.add(strings.get(j - 1) + " : " + strings.get(j + 1));
                        strings辅助.set(j - 1, "");
                        strings辅助.set(j, "");
                        strings辅助.set(j + 1, "");
                    });
            String 非缩写语法字符串 = strings辅助.stream().map(String::toString).collect(Collectors.joining(" "));
            非缩写语法字符串 = Arrays.stream(非缩写语法字符串.split("\\s+")).filter(i -> ! i.equals(",")).collect(Collectors.joining(" "));


            // 1.1 计算'非缩写语法段'的值
            Double 值1 = (Double) SUM(公式id, 非缩写语法字符串, day);


            // 1.2 计算'缩写语法段'的值
            Double 缩写公式体的结果;
            Double sum = 0d;
            for (String one : 缩写语法段) {
                String[] vs = one.split("\\s+");
                String 第一个公式体 = vs[0];
                String 最后一个公式体 = vs[2];
                GongShiDTO aaa = GongShiDTO.getInstance(第一个公式体);
                GongShiDTO bbb = GongShiDTO.getInstance(最后一个公式体);
                String tableName = aaa.tableName;
                String columnName = aaa.colName;
                Integer startCode = Integer.valueOf(aaa.rowName);
                Integer endCode = Integer.valueOf(bbb.rowName);

                String sql = String.format("SELECT SUM(%s) FROM %s WHERE CODE>=%s AND CODE<=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, startCode, endCode, day);
                System.out.print(sql + " = ");
                Double v = jdbcTemplate.queryForObject(sql, Double.class);
                v = v == null ? 0 : v;
                System.out.println(v);
                sum += v;
            }
            缩写公式体的结果 = sum;
            Double 值2 = 缩写公式体的结果;


            // 1.3 相加
            Double v = 值1 + 值2;

            return v;
        }

        // 2. 逐个累加
        else {
            int 左括号位置 = strings.indexOf("(");
            int 右括号位置 = strings.indexOf(")");
            List<String> 需要累加的公式体列表 = strings.subList(左括号位置 + 1, 右括号位置);

            List<String> 常数列表 = 需要累加的公式体列表.stream().filter(i -> ! i.contains("#")).collect(Collectors.toList());
            List<String> 公式体列表 = 需要累加的公式体列表.stream().filter(i -> i.contains("#")).collect(Collectors.toList());


            // 2.1 计算常数列表的值
            Double 常数汇总值;
            if (常数列表.size() > 0) {
                常数汇总值 = 常数列表.stream().mapToDouble(Double::parseDouble).sum();
                System.out.println(常数列表.stream().collect(Collectors.joining(" + ")) + " = " + 常数汇总值);
            }
            else {
                常数汇总值 = 0d;
            }


            // 2.2 计算公式体列表的值
            Double 公式体汇总值;
            if (公式体列表.size() > 0) {
                GongShiDTO aaa = GongShiDTO.getInstance(公式体列表.get(0));
                String tableName = aaa.tableName;
                String columnName = aaa.colName;
                String codes = 公式体列表.stream().map(i -> i.split("#")[1]).collect(Collectors.joining(","));
                String sql = String.format("SELECT SUM(%s) FROM %s WHERE CODE IN (%s) AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, codes, day);
                System.out.print(sql + " = ");
                公式体汇总值 = jdbcTemplate.queryForObject(sql, Double.class);
                公式体汇总值 = 公式体汇总值 == null ? 0 : 公式体汇总值;
                System.out.println(公式体汇总值);
            }
            else {
                公式体汇总值 = 0d;
            }


            // 2.3 相加
            Double value = 常数汇总值 + 公式体汇总值;

            return value;
        }
    }




    @Override
    public Object SUMIF(Object 公式id, String SUMIF的公式, String day) {
        List<String> strings = Arrays.asList(SUMIF的公式.trim().split("\\s+"));

        // 1. 取出每一个单元格的值, 放入'参照值队列',用来和'参数2'作对比
        List<String> 参数1 = strings.subList(2, strings.indexOf(","));
        String 参数1_第一个公式体 = 参数1.get(0);
        String 参数1_最后一个公式体 = 参数1.get(2);

        GongShiDTO aaa = GongShiDTO.getInstance(参数1_第一个公式体);
        GongShiDTO bbb = GongShiDTO.getInstance(参数1_最后一个公式体);
        String tableName = aaa.tableName;
        String columnName = aaa.colName;
        int startCode = Integer.valueOf(aaa.rowName);
        int endCode = Integer.valueOf(bbb.rowName);

        String sql = String.format("SELECT CODE, %s AS VALUE FROM %s WHERE CODE>=%s AND CODE<=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, startCode, endCode, day);
        System.out.print(sql + " = ");
        List<CodeAndValue> 参照值队列 = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CodeAndValue.class));



        // 2. 比较的基数
        String 参数2 = strings.subList(strings.indexOf(",") + 1, strings.lastIndexOf(",")).get(0);
        Double v2 = (Double) 计算(公式id, 参数2, day);


        // 3. 取出每一个单元格的值, 放入'累加值队列'
        List<String> 参数3 = strings.subList(strings.lastIndexOf(",") + 1, strings.lastIndexOf(")"));
        String 参数3_第一个公式体 = 参数3.get(0);
        String 参数3_最后一个公式体 = 参数3.get(2);
        aaa = GongShiDTO.getInstance(参数3_第一个公式体);
        bbb = GongShiDTO.getInstance(参数3_最后一个公式体);
        tableName = aaa.tableName;
        columnName = aaa.colName;
        startCode = Integer.valueOf(aaa.rowName);
        endCode = Integer.valueOf(bbb.rowName);
        sql = String.format("SELECT CODE, %s AS VALUE FROM %s WHERE CODE>=%s AND CODE<=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, startCode, endCode, day);
        System.out.print(sql + " = ");
        List<CodeAndValue> 累加值队列 = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CodeAndValue.class));


        // 4. 比较后取值
        List<Double> 待累加队列 = new ArrayList<>();
        for (CodeAndValue one : 参照值队列) {
            if ((one.value == null && v2 != null) || (one.value != null && v2 == null) || v2.equals(one.value)) {
                int code = one.code;
                Double found = 累加值队列.stream().filter(i -> i.code.equals(code)).findFirst().get().value;
                if (found == null) {
                    continue;
                }
                待累加队列.add(found);
            }
            // TODO what is it?

            //            int code = one.code;
            //            CodeAndValue found = 累加值队列.stream().filter(i -> i.code.equals(code)).findFirst().orElse(null);
            //            if (found == null || found.value == null) {
            //                continue;
            //            }
            //            待累加队列.add(found.value);
        }

        // 5. 计算累加值
        double 最终值 = 待累加队列.stream().mapToDouble(Double::doubleValue).sum();

        return 最终值;
    }




    @Override
    public Object SUMPRODUCT(Object 公式id, String SUMPRODUCT的公式, String day) {
        List<String> strings = Arrays.asList(SUMPRODUCT的公式.trim().split("\\s+"));

        // 1. 取出每一个单元格的值, 放入'队列1',
        List<String> 参数1 = strings.subList(2, strings.indexOf(","));
        String 参数1_第一个公式体 = 参数1.get(0);
        String 参数1_最后一个公式体 = 参数1.get(2);
        GongShiDTO aaa = GongShiDTO.getInstance(参数1_第一个公式体);
        GongShiDTO bbb = GongShiDTO.getInstance(参数1_最后一个公式体);
        String tableName = aaa.tableName;
        String columnName = aaa.colName;
        int startCode = Integer.parseInt(aaa.rowName);
        int endCode = Integer.parseInt(bbb.rowName);
        String sql = String.format("SELECT CODE, %s AS VALUE FROM %s WHERE CODE>=%s AND CODE<=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, startCode, endCode, day);
        System.out.print(sql + " = ");
        List<CodeAndValue> 队列1 = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CodeAndValue.class));
        List<String> 剩余部分 = strings.subList(strings.indexOf(",") + 1, strings.size());


        // 2. 取出每一个单元格的值, 放入'队列2',
        List<String> 参数2 = 剩余部分.subList(0, 剩余部分.indexOf(")"));
        String 参数2_第一个公式体 = 参数2.get(0);
        String 参数2_最后一个公式体 = 参数2.get(2);
        GongShiDTO ccc = GongShiDTO.getInstance(参数2_第一个公式体);
        GongShiDTO ddd = GongShiDTO.getInstance(参数2_最后一个公式体);
        tableName = ccc.tableName;
        columnName = ddd.colName;
        startCode = Integer.parseInt(ccc.rowName);
        endCode = Integer.parseInt(ddd.rowName);
        sql = String.format("SELECT CODE, %s AS VALUE FROM %s WHERE CODE>=%s AND CODE<=%s AND DAY = TO_DATE('%s', 'yyyy-MM-dd') ", columnName, tableName, startCode, endCode, day);
        System.out.print(sql + " = ");
        List<CodeAndValue> 队列2 = jdbcTemplate.query(sql, new BeanPropertyRowMapper(CodeAndValue.class));
        剩余部分 = 剩余部分.subList(剩余部分.indexOf(")") + 1, 剩余部分.size());


        // 3. 队列1 和 队列2 列列相乘
        List<Double> 列列相乘的值 = new ArrayList<>();
        for (CodeAndValue one : 队列1) {
            if (one.value == null) {
                continue;
            }
            CodeAndValue two = 队列2.stream().filter(i -> i.code.equals(one.code)).findFirst().get();
            if (two == null || two.value == null) {
                continue;
            }
            列列相乘的值.add(one.value * two.value);
        }


        // 4. 将相乘的值相加
        double v1 = 列列相乘的值.stream().mapToDouble(Double::doubleValue).sum();


        // 5. 剩余的其他公式或算术公式
        String 剩余部分公式字符串 = v1
                + " "
                + 剩余部分.stream().map(String::toString).collect(Collectors.joining(" "));

        Object 最终值 = 计算(公式id, 剩余部分公式字符串, day);


        return 最终值;
    }






    @Override
    public int 修改单元格数据(String tableNameCN, Object 不同数据表的行数据id, String columnName, String 新值) {
        String tableName = GongShiCache.getTableName(tableNameCN);


        // 1. 获取表结构映射
        String 旧值sql = String.format("SELECT %s, CODE, NAME, DAY FROM %s WHERE ID = %s", columnName, tableName, 不同数据表的行数据id);
        Map<String, Object> map = jdbcTemplate.queryForMap(旧值sql);
        Object 旧值 = map.get(columnName);
        旧值 = 旧值 == null ? 0 : 旧值;
        String rowCode = map.get("CODE").toString();
        String rowName = map.get("NAME").toString();
        String day = new DateTime(map.get("DAY")).toString("yyyy-MM-dd");


        // 若 新值 === 旧值, 不往下执行
        if (新值.equals(旧值.toString())) {
            return 1;
        }



        // 2. 参数
        String 当前单元格 = tableNameCN + "#" + rowCode + "#" + columnName;
        TreeMap<String, Object> 已取值公式集合 = new TreeMap<>();
        已取值公式集合.put(当前单元格, 新值);
        int 层次 = 1;
        List<String> logList = new ArrayList<>();



        // 3. 修改目标单元格
        String updateSql = String.format("UPDATE %s SET %s = %s WHERE ID = %s", tableName, columnName, (新值.length() == 0 || 新值.equals("NaN")) ? "NULL" : 新值, 不同数据表的行数据id);
        jdbcTemplate.update(updateSql);



        // 4. 记录执行过程开头
        long startTime = System.currentTimeMillis();
        String title = String.format("将「%s %s %s」由 %s 修改为 %s", tableNameCN, rowName, columnName, 旧值, 新值.length() == 0 ? "NULL" : 新值);
        title = title.replace('/', ' ').replace('\\', ' ');  // 替换文件标题的特殊字符
        logList.add(title + "  (  " + updateSql + "  )");



        // 5. 联动修改关联单元格
        根据公式关联更新受到影响的单元格(当前单元格, 已取值公式集合, day, 层次, logList);



        // 6. 记录执行过程结尾
        long endTime = System.currentTimeMillis(); //获取结束时间
        String 耗时 = "程序运行时间： " + (endTime - startTime) / 1000 + "秒";
        System.out.println(耗时);
        logList.add(耗时);
        logList.add("修改单元格数据结束 (说明: 单元格最终值的修改语句,是最后一次的UPDATE语句所修改)");



        // 6. 保存执行过程到文件
        String logContent = logList.stream().map(String::toString).collect(Collectors.joining("\n"));
        saveLogFile(title, logContent);


        return 1;

    }




    private void saveLogFile(String title, String logContent) {
        String logFileName = DateTime.now().toString("yyyy年MM月dd日HH时mm分ss秒SSS毫秒 ") + title + ".log";
        File convertFile = new File("D:/效益测算的联动更新执行记录/" + logFileName);
        try {
            //            System.out.println(logFileName);
            convertFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(logContent.getBytes());
            fout.close();
            System.out.println("执行过程文件已保存");
        } catch (IOException e) {
            // 如果不存在该文件夹,则不保存日志文件
            System.out.println("不存在目录 D:/效益测算的联动更新执行记录/ 跳过保存文件步骤");
        }
    }



    private void 根据公式关联更新受到影响的单元格(String 单元格, TreeMap<String, Object> 已取值公式集合, String day, int 层次, List<String> logList) {
        List<GongShi> 所有公式数据 = GongShiCache.获取所有公式数据();

        List<GongShi> 受到触发的公式List = 所有公式数据.stream()
                .filter(i -> ! StringUtils.isEmpty(i.公式的项目完整体))
                .filter(i -> ! StringUtils.isEmpty(i.单元格公式))
                .filter(i -> Arrays.asList(i.公式的项目完整体.split(",")).contains(单元格))
                .collect(Collectors.toList());

        String 空格缩进 = "";
        for (int i = 0; i < 层次; i++) {
            空格缩进 += "   ";
        }



        if (受到触发的公式List.size() == 0) {
            String output = 空格缩进 + 层次 + ". " + "☆" + 单元格 + " 无关联公式, 不需要修改";
            logList.add(output);
            return;
        }




        for (GongShi one : 受到触发的公式List) {

            // 1. 将值代入公式
            String[] 代入值前的公式元素 = one.单元格公式.split("\\s+");
            for (int index = 0; index < 代入值前的公式元素.length; index++) {
                if (代入值前的公式元素[index].contains("#")) {
                    final String search_key = 代入值前的公式元素[index];
                    Map.Entry<String, Object> entry = 已取值公式集合.entrySet().stream().filter(i -> i.getKey().equals(search_key)).findFirst().orElse(null);
                    if (entry != null) {
                        //                        代入值前的公式元素[index] =  String.valueOf(entry.getValue());
                        代入值前的公式元素[index] = 代入值前的公式元素[index] + "#" + String.valueOf(entry.getValue());
                    }
                }
            }
            String 代入值后的公式 = Arrays.stream(代入值前的公式元素).map(String::toString).collect(Collectors.joining(" "));



            // 2. 取得新值
            System.out.println("代入值后的公式 → " + 代入值后的公式);
            Object 新值 = 计算(one.id, 代入值后的公式, day);
            if (新值 == null || StringUtils.isEmpty(新值.toString()) || 新值.toString().equals("NaN") || 新值.toString().toUpperCase().equals("INFINITY")) {
                新值 = "0";
            }



            // 3. 将公式及其值临时存放,用作公式值代入
            已取值公式集合.put(one.单元格, 新值);



            // 4. 获取单元格的表结构映射
            GongShiDTO 映射 = GongShiCache.getTableList().stream().filter(i -> i.biaoMing.equals(one.表)).findFirst().get();
            String tableName = 映射.tableName;
            String columnName = 映射.columns.stream().filter(i -> i.lieMing.equals(one.列)).findFirst().get().colName;


            // 5. 查询旧值
            String 旧值sql = String.format("SELECT \"%s\" FROM %s WHERE CODE = %s AND DAY = TO_DATE('%s', 'yyyy-MM-dd')", columnName, tableName, one.getCode(), day);
            System.out.println(旧值sql);
            Double 旧值 = jdbcTemplate.queryForObject(旧值sql, Double.class);
            if (旧值 == null) {
                旧值 = 0d;
            }


            // 6. 组成 update 语句
            String updateSql = String.format("UPDATE %s SET \"%s\" = %s WHERE CODE = %s AND DAY = TO_DATE('%s', 'yyyy-MM-dd')", tableName, columnName, 新值, one.code, day);


            // 7. 记录执行过程
            if (旧值.doubleValue() == Double.valueOf(新值.toString()).doubleValue()) {
                String output = 空格缩进 + 层次 + ". " + "☆" + one.getDisplayText() + String.format("  →  由  「 %s  修改为  %s 」  ←  %s  ⇢  %s", 旧值, 新值, "新值和旧值相同,不需要进行本行的修改,该行对应的子层修改也被停止", updateSql);
                logList.add(output);
                continue;
            }
            else {
                String output = 空格缩进 + 层次 + ". " + one.getDisplayText() + String.format("  →  由  「 %s  修改为  %s 」  ←  %s", 旧值, 新值, updateSql);
                logList.add(output);

                // 8. 执行 update 语句
                jdbcTemplate.update(updateSql);
            }






            // 9. 继续探寻修改
            根据公式关联更新受到影响的单元格(one.单元格, 已取值公式集合, day, 层次 + 1, logList);
        }
        层次++;
    }





    // TODO 开发用_填充关联的对象编码
    @Override
    public int 开发用_填充关联的对象编码() {

        // 1. 包含冒号的缩写公式
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(GongShi::get单元格公式, ":").orderByAsc(GongShi::get表, GongShi::getSort);
        List<GongShi> list1 = list(queryWrapper);
        for (GongShi one : list1) {
            String v1 = 返回单元格公式的项目完整体(one.单元格公式);
            one.公式的项目完整体 = v1;
        }
        for (GongShi one : list1) {
            updateById(one);
        }



        // 2. 不带冒号的公式
        LambdaQueryWrapper<GongShi> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.notLike(GongShi::get单元格公式, ":").orderByAsc(GongShi::get表, GongShi::getSort);
        List<GongShi> list2 = list(queryWrapper2);
        for (GongShi one : list2) {
            List<String> strings1 = Arrays.asList(one.单元格公式.split("\\s+"));
            List<String> filterList = strings1.stream().filter(i -> i.split("#").length == 3).collect(Collectors.toList());
            String v1 = filterList.stream().map(String::toString).collect(Collectors.joining(","));
            one.公式的项目完整体 = v1;
        }
        for (GongShi one : list2) {
            updateById(one);
        }


        return 1;
    }





    @Override
    public String 返回完整的项目公式体(String 单元格公式) {
        if (单元格公式.contains(":")) {
            // 1. 包含冒号的缩写公式
            String v1 = 返回单元格公式的项目完整体(单元格公式);
            return v1;
        }
        else {
            // 2. 不带冒号的公式
            List<String> strings1 = Arrays.asList(单元格公式.split("\\s+"));
            List<String> filterList = strings1.stream().filter(i -> i.split("#").length == 3).collect(Collectors.toList());
            String v1 = filterList.stream().map(String::toString).collect(Collectors.joining(","));
            return v1;
        }
    }






    private String 返回单元格公式的项目完整体(String 单元格公式) {
        List<String> list1 = Arrays.asList(单元格公式.split("\\s+")).stream().filter(i -> i.split("#").length == 3 || i.equals(":")).collect(Collectors.toList());
        String str1 = list1.stream().map(String::toString).collect(Collectors.joining(" "));


        List<String> list2 = Arrays.asList(str1.split(" "));
        String str2 = list2.stream().map(String::toString).collect(Collectors.joining(",")).replace(",:,", ":");


        String[] list3 = str2.split(",");
        List<String> 纯公式体列表 = new ArrayList<>();
        for (String one : list3) {
            if (one.contains(":")) {
                String 第一个公式体 = one.split(":")[0].trim();
                String 第二个公式体 = one.split(":")[1].trim();
                GongShiDTO aaa = GongShiDTO.getInstance(第一个公式体);
                GongShiDTO bbb = GongShiDTO.getInstance(第二个公式体);
                String tableNameCN = aaa.biaoMing;
                String colNameCN = aaa.lieMing;
                Integer startCode = Integer.valueOf(aaa.rowName);
                Integer endCode = Integer.valueOf(bbb.rowName);
                String sql = String.format("SELECT CODE FROM HZSH_PEI_ZHI WHERE CODE>=%s AND CODE<=%s", startCode, endCode);
                List<String> codeList = jdbcTemplate.queryForList(sql, String.class);

                List<String> itemList = new ArrayList<>();
                for (String _c : codeList) {
                    String item = String.format("%s#%s#%s", tableNameCN, _c, colNameCN);
                    itemList.add(item);
                }

                String xxx = itemList.stream().map(String::toString).collect(Collectors.joining(","));
                纯公式体列表.add(xxx);
            }
            else {
                纯公式体列表.add(one);
            }
        }


        String result = 纯公式体列表.stream().map(String::toString).collect(Collectors.joining(","));
        return result;
    }






    //#region 卜祥迎 2021年12月15日

    @Override
    public String importExcel(String fileName, MultipartFile file) throws Exception {
        //        String pattern = "yyyy-MM-dd";
        //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        if (! fileName.matches("^.+\\.(?i)(xls)$") && ! fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        }
        else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null) {
            return "文件为空";
        }
        int lastRowNum = sheet.getLastRowNum();// 读取最后一条数据的下标（没有数据的那一行）

        int updateNum = 0;
        int insertNum = 0;
        int errorNum = 0;
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("\n" + "详情：");
        for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
            GongShi gongShi = new GongShi();
            gongShi.setName("excel导入");
            try {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                try {
                    // 获取类型数据
                    String dtg = getRowString(row.getCell(0));// 单元格
                    if (dtg != null) {
                        gongShi.单元格 = dtg;
                        String code = dtg.split("#")[1];
                        gongShi.code = code;
                        // 从[PEI_ZHI]表获取排序
                        PeiZhi peiZhi = peiZhiService.getUnique(code);
                        gongShi.sort = peiZhi.sort;
                    }
                    String dygzgz = getRowString(row.getCell(1));// 单元格直观值
                    if (dygzgz != null) {
                        //gongShi.单元格直观值=dygzgz;
                        String 表 = dygzgz.split("#")[0];
                        String 行 = dygzgz.split("#")[1];
                        String 列 = dygzgz.split("#")[2];
                        gongShi.表 = 表;
                        gongShi.行 = 行;
                        gongShi.列 = 列;
                    }
                    String gs = getRowString(row.getCell(2));// 公式
                    if (row.getCell(2) != null) {
                        gongShi.单元格公式 = gs;
                    }

                    String gszg = getRowString(row.getCell(3));// 直观公式
                    if (gszg != null) {
                        gongShi.单元格公式直观值 = gszg;
                    }

                    if (gongShi.getCode() == null || "".equals(gongShi.getCode())) {
                        continue;
                    }

                    boolean exist;
                    QueryWrapper<GongShi> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(GongShi::getCode, gongShi.getCode()).eq(GongShi::get列, gongShi.get列());
                    List<GongShi> result = gongShiMapper.selectList(queryWrapper);
                    if (! result.isEmpty() && result.size() > 0) {
                        exist = true;
                    }
                    else {
                        exist = false;
                    }
                    if (exist) {// 若有此编码的记录,则更新
                        gongShi.setId(result.get(0).getId());
                        this.updateById(gongShi);
                        updateNum += 1;
                        sBuffer.append("\n更新第" + rowNum + "行，" + gongShi.get单元格());
                    }
                    else {
                        this.save(gongShi);
                        insertNum += 1;
                        sBuffer.append("\n新增第" + rowNum + "行，" + gongShi.get单元格());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } // 查询数据库是否有此编码的记录

            } catch (Exception e) {
                e.printStackTrace();
                errorNum++;
                sBuffer.append("\n第" + rowNum + "行操作失败，" + gongShi.get单元格() + "，失败原因：" + e.getMessage());
            }
        }
        String result = "总" + (updateNum + insertNum) + "行记录，插入" + insertNum + "行，更新" + updateNum + "行，失败" + errorNum + "行";
        result = result;
        return result;
    }




    /**
     * 辅助excel文件操作 从cell中取出String类型数据
     *
     * @param cell
     * @return
     */
    String getRowString(Cell cell) {
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            String rowString = null;
            rowString = cell.getStringCellValue();
            if (rowString != null && "".equals(rowString.trim())) return null;
            else return rowString;
        }
        else {
            return null;
        }
    }

    //#endregion


}
