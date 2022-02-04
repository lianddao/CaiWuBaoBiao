package com.hzsh.zhks.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hzsh.hzsh.model.PeiZhi;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 中海开氏-产品成本  [ZHKS_CHANPIN_CHENGBEN]
 */
@Data
@TableName("ZHKS_CHANPIN_CHENGBEN")
public class KaiShiChanPinChengBen {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * ★ 数据所属的月份.作为数据唯一性区别
     */
    @TableField(value = "DAY")
    public Date month;

    /**
     * 编码
     */
    public String code;

    @TableField(exist = false)
    public String category;

    /**
     * 名称
     */
    public String name;

    /**
     * 生产数量
     */
    @TableField(value = "\"生产数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产数量;

    /**
     * 生产单价
     */
    @TableField(value = "\"生产单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产单价;

    /**
     * 生产金额
     */
    @TableField(value = "\"生产金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产金额;

    /**
     * 生产系数
     */
    @TableField(value = "\"生产系数\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产系数;

    /**
     * 生产积数
     */
    @TableField(value = "\"生产积数\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产积数;

    /**
     * 销售数量
     */
    @TableField(value = "\"销售数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 销售数量;

    /**
     * 销售单价
     */
    @TableField(value = "\"销售单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 销售单价;

    /**
     * 销售金额
     */
    @TableField(value = "\"销售金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 销售金额;

    /**
     * 排序
     */
    public Integer sort;



    //#region   扩展

    public String getMonthString() {
        if (month == null) {
            return DateTime.now().toString("yyyy-MM");
        }
        else {
            return new DateTime(month).toString("yyyy-MM");
        }
    }

    public String getClassName() {
        return "KaiShiChanPinChengBen";
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
