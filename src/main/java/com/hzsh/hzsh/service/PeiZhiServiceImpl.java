package com.hzsh.hzsh.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzsh.hzsh.mapper.PeiZhiMapper;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.model.PeiZhi;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <h1>编码配置 [HZSH_PEI_ZHI]</h1>
 */
@Service
@Component
public class PeiZhiServiceImpl extends ServiceImpl<PeiZhiMapper, PeiZhi> implements PeiZhiService {


    /**
     * 从Excel导入数据时,新增或修改到 [HZSH_PEI_ZHI]
     */
    @Override
    public List<PeiZhi> autoInsertOrUpdate(Object data_list) {
        List<PeiZhi> result = new ArrayList<>();

        String listString = JSON.toJSONString(data_list);
        List<PeiZhi> peiZhiList = JSON.parseArray(listString, PeiZhi.class);
        for (PeiZhi one : peiZhiList) {
            //            PeiZhi dbOne = getUnique(one.category, one.name);
            PeiZhi dbOne = getUnique(one.category, one.name, one.sort, one.isHidden);
            if (dbOne == null) {
                super.save(one);
            }
            else {
                one.id = dbOne.id;
                super.updateById(one);
            }

            result.add(one);
        }

        return result;
    }


    /**
     * 获取唯一对象
     */
    @Override
    public PeiZhi getUnique(String code) {
        LambdaQueryWrapper<PeiZhi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PeiZhi::getCode, code);

        PeiZhi one = baseMapper.selectOne(queryWrapper);

        return one;
    }


    /**
     * 从excel底稿导入时,根据目录和名称判断唯一性,(sort用来处理名称相同的项)
     */
    @Override
    public PeiZhi getUnique(String category, String name, Integer sort, Boolean isHidden) {
        LambdaQueryWrapper<PeiZhi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PeiZhi::getCategory, category)
                .eq(PeiZhi::getName, name)
                .eq(PeiZhi::getSort, sort)
                .eq(PeiZhi::getIsHidden, isHidden);

        PeiZhi one = baseMapper.selectOne(queryWrapper);

        return one;
    }

    /**
     * 获取目录数据
     */
    @Override
    public List<String> getCategoryList() {
        QueryWrapper<PeiZhi> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(PeiZhi::getCode);
        List<PeiZhi> list = baseMapper.selectList(queryWrapper);

        // 将目录按顺序存放
        List<String> categoryList = new ArrayList<>();
        for (PeiZhi one : list) {
            if (! categoryList.contains(one.category)) {
                categoryList.add(one.category);
            }
        }

        return categoryList;
    }





    @Override
    public int edit(String name, String value, Long pk) {
        if (StringUtils.isEmpty(value)) {
            // ①. 若设置空值, 数字类型的值需要这里特别处理. (字符串类型也适用)
            Field field = Arrays.stream(PeiZhi.class.getDeclaredFields()).filter(i -> i.getName().equals(name)).findFirst().get();
            TableField tableField = field.getAnnotation(TableField.class);
            String 数据库列名 = tableField == null ? name : tableField.value();
            UpdateWrapper<PeiZhi> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("id", pk)
                    .set(数据库列名, value);
            update(updateWrapper);
        }
        else {
            // ②. 非空值的修改
            Map map = new HashMap();
            map.put(name, value);
            PeiZhi one = JSON.parseObject(JSON.toJSONString(map), PeiZhi.class);
            one.id = getById(pk).id;
            updateById(one);
        }

        return 1;
    }



    /**
     * 获取指定目录的配置
     */
    @Override
    public List<PeiZhi> getByCategory(String category) {
        LambdaQueryWrapper<PeiZhi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(PeiZhi::getSort);
        queryWrapper.eq(PeiZhi::getCategory, category);
        List<PeiZhi> list = baseMapper.selectList(queryWrapper);

        return list;
    }



    /**
     * 新增时, 准备一个编码
     */
    @Override
    public int getAutoCode(String category) {
        QueryWrapper<PeiZhi> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("MAX(CODE)").lambda().eq(PeiZhi::getCategory, category);
        Map<String, Object> map = getMap(queryWrapper);
        String code = map.get("MAX(CODE)").toString();

        int newCode = Integer.parseInt(code) + 1;
        System.out.println("提供待使用的编码 = " + newCode);

        return newCode;
    }



    /**
     * 新增一个
     */
    @Override
    public PeiZhi insertOne(PeiZhi one) {
        one.isHidden = false;
        one.isBold = false;
        save(one);

        return one;
    }



}
