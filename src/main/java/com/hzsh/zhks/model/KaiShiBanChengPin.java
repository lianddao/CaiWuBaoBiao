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
 * 中海开氏-半成品  [ZHKS_BAN_CHENG_PIN]
 */
@Data
@TableName("ZHKS_BAN_CHENG_PIN")
public class KaiShiBanChengPin {

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
     * 月初数量
     */
    @TableField(value = "\"月初数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月初数量;

    /**
     * 月初单价
     */
    @TableField(value = "\"月初单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月初单价;

    /**
     * 月初金额
     */
    @TableField(value = "\"月初金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月初金额;

    /**
     * 月末数量
     */
    @TableField(value = "\"月末数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月末数量;

    /**
     * 月末单价
     */
    @TableField(value = "\"月末单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月末单价;

    /**
     * 月末金额
     */
    @TableField(value = "\"月末金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 月末金额;

    /**
     * 排序
     */
    public Integer sort;





    //#region 扩展

    public String getMonthString() {
        if (month == null) {
            return DateTime.now().toString("yyyy-MM");
        }
        else {
            return new DateTime(month).toString("yyyy-MM");
        }
    }

    public String getClassName() {
        return "KaiShiBanChengPin";
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
