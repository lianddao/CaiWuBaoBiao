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
 * 中海开氏-生产经营  [ZHKS_SHENGCHAN_JINGYIN]
 */
@Data
@TableName("ZHKS_SHENGCHAN_JINGYIN")
public class KaiShiShengChanJingYin {

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
     * 序号
     */
    public String xuHao;

    /**
     * 名称
     */
    public String name;

    /**
     * 单位
     */
    public String unit;

    /**
     * 数量
     */
    @TableField(value = "\"数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 数量;

    /**
     * 单价
     */
    @TableField(value = "\"单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 单价;

    /**
     * 金额(万元)
     */
    @TableField(value = "\"金额万元\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 金额万元;

    /**
     * 吨油成本
     */
    @TableField(value = "\"吨油成本\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 吨油成本;

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
        return "KaiShiShengChanJingYin";
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
