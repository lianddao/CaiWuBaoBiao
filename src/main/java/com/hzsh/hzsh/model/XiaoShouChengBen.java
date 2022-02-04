package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;



/**
 * 销售成本 - HZSH_XIAOSHOU_CHENGBEN
 */
@Data
@TableName("HZSH_XIAOSHOU_CHENGBEN")
public class XiaoShouChengBen {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * 数据所属的日期.作为数据唯一性区别
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date day;

    /**
     * 编码
     */
    public String code;

    /**
     * 项目
     */
    public String name;

    /**
     * SAP物料号
     */
    public Integer sapCode;

    @TableField(value = "\"本期生产数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期生产数量;

    @TableField(value = "\"本期生产系数\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期生产系数;

    @TableField(value = "\"本期生产积数\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期生产积数;

    @TableField(value = "\"本期生产单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期生产单价;

    @TableField(value = "\"本期生产金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期生产金额;

    @TableField(value = "\"本期销售数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售数量;

    @TableField(value = "\"本期销售单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售单价;

    @TableField(value = "\"本期销售金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售金额;


    /**
     * Excel中的行索引
     */
    public Integer sort;





    //#region   扩展
    @TableField(exist = false)
    public String category;


    public String getDayString() {
        if (day == null) {
            return DateTime.now().toString("yyyy-MM-dd");
        }
        else {
            return new DateTime(day).toString("yyyy-MM-dd");
        }
    }

    public String getClassName() {
        return "XiaoShouChengBen";
    }


    public String get中文表名() {
        return "销售成本";
    }


    @TableField(exist = false)
    public Boolean isBold;

    @TableField(exist = false)
    public Boolean isHidden;

    /**
     * 配置
     */
    @TableField(exist = false)
    public PeiZhi peiZhi = new PeiZhi();
    //#endregion




}
