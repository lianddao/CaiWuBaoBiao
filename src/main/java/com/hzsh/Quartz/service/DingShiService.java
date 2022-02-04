package com.hzsh.Quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.Quartz.entity.DingShi;

import java.util.List;

public interface DingShiService extends IService<DingShi> {

    List<DingShi> defaultList();


    /**
     * 修改
     */
    int edit(String name, String value, String pk);


    int 执行效益测算的默认定时任务();


    int 执行效益测算的默认定时任务(String 本次定时任务保存的数据日期);


    int 测试用_复制底稿数据到当前日期();


    int 测试用_删除复制的当前日期的底稿数据();
}
