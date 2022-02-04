package com.hzsh.Quartz.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.Quartz.entity.DingShi;
import com.hzsh.Quartz.mapper.DingShiMapper;
import com.hzsh.hzsh.controller.GongShiCache;
import com.hzsh.hzsh.dto.GongShiDTO;
import com.hzsh.hzsh.model.*;
import com.hzsh.hzsh.service.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 定时任务
 */
@Service
public class DingShiServiceimpl extends ServiceImpl<DingShiMapper, DingShi> implements DingShiService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private GongShiService gongShiService;
    @Resource
    private LiRunService liRunService;
    @Resource
    private XiaoShouShouRuService xiaoShouShouRuService;
    @Resource
    private XiaoShouChengBenService xiaoShouChengBenService;
    @Resource
    private JingYingFeiYongService jingYingFeiYongService;
    @Resource
    private ShuiJinService shuiJinService;
    @Resource
    private YuanCaiLiaoService yuanCaiLiaoService;
    @Resource
    private BanChengPinService banChengPinService;
    @Resource
    private FeiYongMingXiService feiYongMingXiService;






    @Override
    public List<DingShi> defaultList() {
        LambdaQueryWrapper<DingShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(DingShi::getState);
        return list(queryWrapper);
    }



    /**
     * 修改
     */
    @Override
    public int edit(String name, String value, String pk) {
        if (StringUtils.isEmpty(value)) {
            // ①. 若设置空值, 数字类型的值需要这里特别处理. (字符串类型也适用)
            Field field = Arrays.stream(DingShi.class.getDeclaredFields()).filter(i -> i.getName().equals(name)).findFirst().get();
            TableField tableField = field.getAnnotation(TableField.class);
            String 数据库列名 = tableField == null ? name : tableField.value();
            UpdateWrapper<DingShi> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("id", pk)
                    .set(数据库列名, value);
            update(updateWrapper);
        }
        else {
            // ②. 非空值的修改
            Map map = new HashMap();
            map.put(name, value);
            DingShi one = JSON.parseObject(JSON.toJSONString(map), DingShi.class);
            one.id = getById(pk).id;
            updateById(one);
        }


        return 1;
    }





    /**
     * <b>业务逻辑</b>
     * <ul style='color:green; font-weight:bold'>
     *     <ol>① 每天的价格表的数据可能会发生变化, 那么则联动更新其触发的单元格</ol>
     * </ul>
     *
     * @return
     */
    @Override
    public int 执行效益测算的默认定时任务() {

        // 1. 日期参数
        String 本次定时任务保存的数据日期为昨天的日期 = DateTime.now().plusDays(- 1).withTimeAtStartOfDay().toString("yyyy-MM-dd");


        // 2.
        执行效益测算的默认定时任务(本次定时任务保存的数据日期为昨天的日期);


        return 1;
    }



    @Override
    public int 执行效益测算的默认定时任务(String 本次定时任务保存的数据日期) {
        // 1. 开始记录运行时长
        long startTime = System.currentTimeMillis();



        // 2. 复制最近日期的数据
        int a = liRunService.复制最近日期的数据(本次定时任务保存的数据日期);
        int b = xiaoShouShouRuService.复制最近日期的数据(本次定时任务保存的数据日期);
        int c = xiaoShouChengBenService.复制最近日期的数据(本次定时任务保存的数据日期);
        int d = jingYingFeiYongService.复制最近日期的数据(本次定时任务保存的数据日期);
        int e = shuiJinService.复制最近日期的数据(本次定时任务保存的数据日期);
        int f = yuanCaiLiaoService.复制最近日期的数据(本次定时任务保存的数据日期);
        int g = banChengPinService.复制最近日期的数据(本次定时任务保存的数据日期);
        int h = feiYongMingXiService.复制最近日期的数据(本次定时任务保存的数据日期);



        // 3. 获取包含"价格表公式"的单元格
        LambdaQueryWrapper<GongShi> gongShiQueryWrapper = new LambdaQueryWrapper<>();
        gongShiQueryWrapper.like(GongShi::get单元格公式, "价格表#").orderByAsc(GongShi::get表, GongShi::getCode);
        List<GongShi> 包含价格表公式的公式对象列表 = gongShiService.list(gongShiQueryWrapper);


        // 4. 查询包含"价格表公式"的行对象
        List<JSONObject> 待更新价格的行集合 = new ArrayList<>();
        for (GongShi gongShi : 包含价格表公式的公式对象列表) {
            String tableNameCN = gongShi.表;
            String tableName = GongShiCache.getTableName(tableNameCN);

            // 从数据表中找到'步骤2复制的'目标行
            String sql = String.format("SELECT * FROM %s WHERE CODE = %s AND DAY = TO_DATE('%s', 'yyyy-MM-dd')", tableName, gongShi.code, 本次定时任务保存的数据日期);
            Map<String, Object> map = jdbcTemplate.queryForMap(sql);

            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
            jsonObject.put("表", tableNameCN);
            jsonObject.put("单元格", tableNameCN + " → " + gongShi.行 + " → " + gongShi.列);
            jsonObject.put("查询旧值", String.format("SELECT %s FROM %s WHERE CODE = %s AND DAY = TO_DATE('%s', 'yyyy-MM-dd')", gongShi.列, tableNameCN, gongShi.code, 本次定时任务保存的数据日期));
            待更新价格的行集合.add(jsonObject);
        }




        // 测试
        //        待更新价格的行集合 = 待更新价格的行集合.stream().filter(i -> i.getString("CODE").equals("16000035")).collect(Collectors.toList());
        //        待更新价格的行集合 = 待更新价格的行集合.stream().filter(i -> i.getString("表").equals("原材料表")).collect(Collectors.toList());
        //        待更新价格的行集合 = 待更新价格的行集合.stream().filter(i -> i.getString("CODE").equals("12000065") || i.getString("CODE").equals("16000035")).collect(Collectors.toList());






        // 5. 触发'价格公式'的更新
        for (JSONObject 具有价格公式的数据行 : 待更新价格的行集合) {
            Object 销售收入或原材料表的数据行id = 具有价格公式的数据行.get("ID");
            Object code = 具有价格公式的数据行.get("CODE");


            // '当前数据行' 的 '与价格表相关的单元格' 的触发影响的公式
            List<GongShi> gsList = 包含价格表公式的公式对象列表.stream().filter(i -> i.code.equals(code)).collect(Collectors.toList());
            for (GongShi gongShi : gsList) {
                // 联动修改单元格
                Object 新值 = gongShiService.计算(gongShi.id, gongShi.单元格公式, 本次定时任务保存的数据日期);

                if (新值 == null || StringUtils.isEmpty(新值.toString()) || 新值.toString().equals("NaN")) {
                    新值 = 0;
                }

                int re = gongShiService.修改单元格数据(gongShi.表, 销售收入或原材料表的数据行id, gongShi.列, String.valueOf(新值));
            }
        }



        // 6. 结束记录运行时长
        long endTime = System.currentTimeMillis();
        String 耗时 = "运行耗时： " + (endTime - startTime) / 1000 + "秒";
        System.out.println("「执行效益测算的默认定时任务」完成. " + 耗时);


        return 1;
    }




    @Override
    public int 测试用_复制底稿数据到当前日期() {
        int a = liRunService.测试用_复制底稿数据到今天();
        int b = xiaoShouShouRuService.测试用_复制底稿数据到今天();
        int c = xiaoShouChengBenService.测试用_复制底稿数据到今天();
        int d = jingYingFeiYongService.测试用_复制底稿数据到今天();
        int e = shuiJinService.测试用_复制底稿数据到今天();
        int f = yuanCaiLiaoService.测试用_复制底稿数据到今天();
        int g = banChengPinService.测试用_复制底稿数据到今天();
        int h = feiYongMingXiService.测试用_复制底稿数据到今天();

        return 1;
    }




    @Override
    public int 测试用_删除复制的当前日期的底稿数据() {
        liRunService.测试用_删除复制的当前日期的底稿数据();
        xiaoShouShouRuService.测试用_删除复制的当前日期的底稿数据();
        xiaoShouChengBenService.测试用_删除复制的当前日期的底稿数据();
        jingYingFeiYongService.测试用_删除复制的当前日期的底稿数据();
        shuiJinService.测试用_删除复制的当前日期的底稿数据();
        yuanCaiLiaoService.测试用_删除复制的当前日期的底稿数据();
        banChengPinService.测试用_删除复制的当前日期的底稿数据();
        feiYongMingXiService.测试用_删除复制的当前日期的底稿数据();

        return 1;
    }





}
