package com.hzsh.Quartz.entity;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import groovy.transform.ToString;

@Component
public class QuartzDeploy {
    private String id;//编号ID

    private String name;//任务名称

    private String cron;//任务执行时间规则
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createtime;//创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatetime;//修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bigentime;//启动时间

    private String status;//状态：0启用、1不启用

    private String remark;//备注
    @Value(value = "${info.app.name}")
    private String projectcode;//项目编码
    @Value(value = "${info.app.info}")
    private String projectname;//项目中文名称


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getBigentime() {
        return bigentime;
    }

    public void setBigentime(Date bigentime) {
        this.bigentime = bigentime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
    

}