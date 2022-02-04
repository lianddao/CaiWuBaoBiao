package com.hzsh.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


public class SupperController<S extends IService,T> {
    //service
    @Autowired
    protected
    S service;

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public R saveOrUpdate(T entity){
        try {
            boolean save = service.saveOrUpdate(entity);
            if(save){
                return R.success(entity);
            }else {
                return R.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * getOneEntity 用于获取单行记录，当多于一行只报错
     * @author silas
     * @date 2019-11-28 9:46
     * @param  -> []
     * @return com.hzsh.util.R
     */
    @RequestMapping("getOne")
    @ResponseBody
    public  R getOneEntity(T searchObj){
        try {
            QueryWrapper<T> wrapper = new QueryWrapper<>(searchObj);
            return  R.success(service.getOne(wrapper));
        }catch (Exception e){
            e.printStackTrace();
            return R.error().msg(e.getMessage());
        }
    }

    //create
    @RequestMapping("save")
    @ResponseBody
    public R save(@RequestBody  T entity){
        try {
            boolean save = service.save(entity);
            if(save){
                return R.success();
            }else {
                return R.error();
            }
        }catch (Exception e){
            e.getMessage();
            return R.error();
        }
    }
    @RequestMapping("listAll")
    @ResponseBody
    public List<T> listAll(){
        try {
            return service.list();
        }catch (Exception e){
            e.getMessage();
            return null;
        }

    }
    //retrieve
    @RequestMapping("pageList")
    @ResponseBody
    public R pageList(Integer current, Integer size,String firstOrder,String orderAscOrder,String orderDescOrder, T searchObj){
        try {
            //分页,默认当前页为 1，单页记录数为 20
            Page<T> page = new Page<>(current==null||current<=0?1:current,size==null||size<=0?20:size);
            //查询构造器
            QueryWrapper<T> wrapper = new QueryWrapper<>(searchObj);
            if(firstOrder!=null&&firstOrder.equals("asc")){//决定ASC在前还DESC在前
                //正序排序
                wrapper.orderByAsc(StringUtils.isNotEmpty(orderAscOrder),orderAscOrder);
                //倒序排序
                wrapper.orderByDesc(StringUtils.isNotEmpty(orderDescOrder),orderDescOrder);
            }else{
                //倒序排序
                wrapper.orderByDesc(StringUtils.isNotEmpty(orderDescOrder),orderDescOrder);
                //正序排序
                wrapper.orderByAsc(StringUtils.isNotEmpty(orderAscOrder),orderAscOrder);
            }
            //执行查询操作
            IPage<T> resultPage = service.page(page,wrapper);
            //返回结果
            return R.success(resultPage);
        }catch (Exception e){
            e.printStackTrace();
            return R.error().msg(e.getMessage());
        }
    }

    //updateById
    @RequestMapping("updateById")
    @ResponseBody
    public R updateById(T entity){
        try {
        	
            boolean updateById = service.updateById(entity);
            if(updateById){
                return R.success();
            }else {
                return R.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }

    //delete
    @RequestMapping("removeById")
    @ResponseBody
    public R removeById(long id){
        try {
            boolean removeById = service.removeById(id);
            if(removeById){
                return R.success();
            }else {
                return R.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error();
        }
    }
    @RequestMapping("deleteById")
    @ResponseBody
    public R deleteById(@RequestParam(value = "ids",required = true) List<String> ids){
        int i=0;
        try {
            for (String id:ids){
                boolean removeById = service.removeById(id);
                if (removeById) {
                    i++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            //return R.error();
        }
        if(i>0){
            return R.success(i);
        }else {
            return R.error();
        }
    }

}
