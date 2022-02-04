package com.hzsh.system.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.Data;
import lombok.ToString;

/**
 * 系统模板
 */
@Data
@ToString
@TableName("SY_MOULD")
public class Mould {//implements Serializable{
	@TableId(type = IdType.ID_WORKER)
	@JsonSerialize(using = ToStringSerializer.class)
	//@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String mouldname;// 模板名称
	public Long morder;// 排序

}