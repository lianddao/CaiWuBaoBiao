package com.hzsh.hzsh.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.ToString;

/**
 * 
 * @ClassName:  Actionlogs    actionlogs
 * @Description:系统操作日志对象
 * @author:
 * @date:
 */
@Data
public class Actionlogs {
	@TableId(type = IdType.ID_WORKER)
	@JsonSerialize(using = ToStringSerializer.class)
    private Long id;    	//编号ID

    private String userid;	//用户ID

    private String username;//用户名称

    private String action;	//操作

    private Date createtime;//开始时间

    private Date endtime;	//结束时间

    private String remark;	//备注

}