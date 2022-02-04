package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.dto.YuanCaiLiaoHuiLvDTO;
import com.hzsh.hzsh.mapper.JiaGeLiShiMapper;
import com.hzsh.hzsh.model.JiaGeLiShi;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 价格历史数据 - [JIA_GE_LISHI]
 */
@Service
public class JiaGeLiShiServiceImpl extends ServiceImpl<JiaGeLiShiMapper, JiaGeLiShi> implements JiaGeLiShiService {


    @Override
    public List<JiaGeLiShi> defaultList(String day) {
        LambdaQueryWrapper<JiaGeLiShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JiaGeLiShi::getDay, DateTime.parse(day).toDate());
        List<JiaGeLiShi> list = list(queryWrapper);

        return list;
    }



    @Override
    public JiaGeLiShi getUnique(String day, String priceCode) {
        LambdaQueryWrapper<JiaGeLiShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(JiaGeLiShi::getDay, DateTime.parse(day).toDate())
                .eq(JiaGeLiShi::getPriceCode, priceCode);

        JiaGeLiShi one = getOne(queryWrapper);
        return one;
    }



    @Override
    public int edit(String name, String value, Long pk) {
        if (StringUtils.isEmpty(value)) {
            // ①. 若设置空值, 数字类型的值需要这里特别处理. (字符串类型也适用)
            Field field = Arrays.stream(JiaGeLiShi.class.getDeclaredFields()).filter(i -> i.getName().equals(name)).findFirst().get();
            TableField tableField = field.getAnnotation(TableField.class);
            String 数据库列名 = tableField == null ? name : tableField.value();
            UpdateWrapper<JiaGeLiShi> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("id", pk)
                    .set(数据库列名, value);
            update(updateWrapper);
        }
        else {
            // ②. 非空值的修改
            Map map = new HashMap();
            map.put(name, value);
            JiaGeLiShi one = JSON.parseObject(JSON.toJSONString(map), JiaGeLiShi.class);
            one.id = getById(pk).id;
            updateById(one);
        }

        return 1;
    }



    @Override
    public boolean insertOrUpdate(JiaGeLiShi one) {
        JiaGeLiShi dbOne = getUnique(one.getDayString(), one.priceCode);
        if (dbOne == null) {
            return super.save(one);
        }
        else {
            one.id = dbOne.getId();
            return super.updateById(one);
        }
    }




    @Override
    public YuanCaiLiaoHuiLvDTO 原材料表页面展示的汇率数据(String day) {
        // 业务逻辑参数
        final String 布伦特 = "1001000026";
        final String 迪拜 = "1001000027";
        final String OmanDubai = "1001000024";
        final String Oman官价 = "1001000028";


        // 获取日期对应的数据
        LambdaQueryWrapper<JiaGeLiShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.inSql(JiaGeLiShi::getPriceCode, 布伦特 + "," + 迪拜 + "," + OmanDubai + "," + Oman官价);
        queryWrapper.eq(JiaGeLiShi::getDay, DateTime.parse(day).toDate());
        List<JiaGeLiShi> list2 = list(queryWrapper);


        YuanCaiLiaoHuiLvDTO dto = new YuanCaiLiaoHuiLvDTO();
        dto.BuLunTe = list2.stream().filter(i -> i.priceCode.equals(布伦特)).findFirst().orElse(null);
        dto.DiBai = list2.stream().filter(i -> i.priceCode.equals(迪拜)).findFirst().orElse(null);
        dto.OmanDubai = list2.stream().filter(i -> i.priceCode.equals(OmanDubai)).findFirst().orElse(null);
        dto.OmanGuanJia = list2.stream().filter(i -> i.priceCode.equals(Oman官价)).findFirst().orElse(null);


        return dto;
    }

}
