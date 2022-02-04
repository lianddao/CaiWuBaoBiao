package com.hzsh.Quartz.service;

import com.hzsh.Quartz.entity.QuartzDeploy;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 任务调度处理
 */
@Service
public class QuartzSchedulers {
    // 任务调度
    @Autowired
    private Scheduler scheduler;
    // 任务调度
    @Autowired
    private QuartzDeploy quartzDeploy;

    @Autowired
    private QuartzDeployService quartzDeployService;

    /**
     * 删除某个任务
     *
     * @param
     * @param
     * @throws SchedulerException
     */
    public String deleteJob(QuartzDeploy quartzDeploy) throws SchedulerException {
        String message = "";
        JobKey jobKey = new JobKey(quartzDeploy.getName(), quartzDeploy.getName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            message = "定时任务：" + quartzDeploy.getName() + "不存在";
        }
        else {
            scheduler.deleteJob(jobKey);
            message = "定时任务：" + quartzDeploy.getName() + "已移除";
        }
        System.out.println(message);
        return message;
    }

    /**
     * 获得任务信息JobDetail
     *
     * @param quartzDeploy
     * @return
     * @throws SchedulerException
     */
    JobDetail getJobDetail(QuartzDeploy quartzDeploy) throws SchedulerException {
        return scheduler.getJobDetail(new JobKey(quartzDeploy.getName(), quartzDeploy.getName()));
    }

    /**
     * 启动某一任务 开始执行定时任务
     *
     * @param
     * @param
     * @throws SchedulerException
     */
    public String startJob(QuartzDeploy quartzDeploy) throws Exception {
        String message = "";
        JobKey jobKey = new JobKey(quartzDeploy.getName(), quartzDeploy.getName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail != null) {// 1.如果任务计划scheduler中已存在此定时任务，则直接恢复启用
            scheduler.resumeJob(jobKey);
            message = "定时任务：" + quartzDeploy.getName() + "已恢复启用=" + quartzDeploy.getCron();
        }
        else if (quartzDeploy.getStatus().equals("1")) {

            // 2.如果任务计划scheduler中不存在此定时任务，则直接启用
            startJobForQuartzDeploy(quartzDeploy);
            message = "定时任务：" + quartzDeploy.getName() + "已启用";
        }
        System.out.println(message);
        return message;
    }

    /**
     * 停止某个任务
     *
     * @param
     * @param
     * @throws SchedulerException
     */
    public String stopJob(QuartzDeploy quartzDeploy) throws SchedulerException {
        String message = "";
        JobKey jobKey = new JobKey(quartzDeploy.getName(), quartzDeploy.getName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            throw new RuntimeException("任务不存在！");
        scheduler.pauseJob(jobKey);
        message = "定时任务：" + quartzDeploy.getName() + "已暂停";
        return message;
    }

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     * @throws ClassNotFoundException
     */
    public String startAllJob() throws Exception {
        String message = "";
        delAllJob();
        toStartScheduleTask();

        scheduler.start();
        message = "已启动任务计划列表中的所有任务";
        System.out.println(message);
        return message;
    }

    // 从数据库中取出所需的任务
    public void toStartScheduleTask() throws Exception {
        System.out.println(quartzDeploy.getProjectcode() + "=======" + quartzDeploy.getProjectname());
        quartzDeploy.setStatus("1");// 状态1，开启定时任务
        quartzDeploy.setProjectcode("hzsh-bigscreen");
        List<QuartzDeploy> quartzDeployList = quartzDeployService.SelectList(quartzDeploy);
        for (QuartzDeploy q : quartzDeployList) {
            startJobForQuartzDeploy(q);
        }
    }

    // 实例化Job，将任务触发器加入任务调度中
    private String startJobForQuartzDeploy(QuartzDeploy quartzDeploy) throws SchedulerException, ClassNotFoundException {
        String message = "";
        String className = "com.hzsh.Quartz.service." + quartzDeploy.getName();
        Class clazz = Class.forName(className);
        // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
        // JobDetail 是具体Job实例
        if (this.getJobDetail(quartzDeploy) != null) {// 如果任务计划scheduler里已存在此任务
            //stopJob(quartzDeploy);
            throw new RuntimeException(quartzDeploy.getName() + "此任务已存在");
        }



        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(quartzDeploy.getName(), quartzDeploy.getName())
                .build();

        // 基于表达式构建触发器
        String Quartzcron = quartzDeploy.getCron();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(Quartzcron);

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzDeploy.getName(), quartzDeploy.getName()).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);

        message = quartzDeploy.getRemark() + "，任务已加入计划列表！（" + className + "）";
        System.out.println(message);
        return message;
    }

    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     *
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();

        if (! oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 删除所有运行的项目
     */
    public void delAllJob() throws SchedulerException {
        List<Map<String, Object>> list = this.queryAllJob();

        for (int i = 0; i < list.size(); i++) {

            Map<String, Object> map1 = list.get(i);
            String jobName = (String) map1.get("jobName");
            String jobGroupName = (String) map1.get("jobGroupName");
            System.out.println(jobName + "===========" + jobGroupName);
            deleteJob(jobName, jobGroupName);
        }
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllJob() {
        List<Map<String, Object>> jobList = null;
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            jobList = new ArrayList<Map<String, Object>>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobName", jobKey.getName());
                    map.put("jobGroupName", jobKey.getGroup());
                    map.put("description", "触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("jobTime", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     *
     * @return
     */
    public List<Map<String, Object>> queryRunJob() {
        List<Map<String, Object>> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String, Object> map = new HashMap<String, Object>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }
}
