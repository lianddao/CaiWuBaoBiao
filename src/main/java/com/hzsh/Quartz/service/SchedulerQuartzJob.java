package com.hzsh.Quartz.service;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzsh.common.utils.ApplicationContextProvider;



/**
 * 此类用于定时任务，以下是具体步骤：
 * 1.新建一个类（如类名：ScheduleJobClassName）继承此虚拟类 
 * 2.实现此类的operationExcute()方法，并在方法体中实现具体业务
 * 3.在配置页面中新增定时任务，注意，任务名要与实现类（ScheduleJobClassName）的类名一致
 * 完成以上三个步骤则定时任务开启成功
 * 
 * com.hzsh.Quartz.service.QuartzSchedulers.toStartScheduleTask()，此方法为实际开启定时任务的方法
 * 具体实现可参考SchedulerQuartzJobICIS.java类的实现
 * @author WuGuoDa
 *
 */
public abstract class SchedulerQuartzJob implements Job{
	
	String actionName ="执行定时任务";//操作名，用于日志记录

	String message = "操作成功";

	

	public void before(){
    }
   
	@Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        before();
        //调用安迅思接口，获取数据,产品 原料计算公式定时任务
        try {
			this.operationExcute();
			System.out.println(actionName);
		} catch (Exception e) {
			e.printStackTrace();
			message = "操作失败，"+e.getMessage();
		}
        after();
    }
	public void after(){

    }

    /**
     * 此方法用于继承类实现具体业务
     * @throws Exception
     */
    public abstract void operationExcute()  throws Exception ; 
}

