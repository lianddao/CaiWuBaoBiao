package com.hzsh.Quartz.service;


import com.hzsh.common.utils.ApplicationContextProvider;



/**
 * 配置需要运行的定时任务
 */
public class SchedulerQuartzJobDataList extends SchedulerQuartzJob {


    @Override
    public void operationExcute() throws Exception {
        DingShiService dingShiService = ApplicationContextProvider.getBean(DingShiService.class);
        dingShiService.执行效益测算的默认定时任务();
    }
}
