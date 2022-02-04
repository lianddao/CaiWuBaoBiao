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
 * 中海开氏-费用  [ZHKS_FEI_YONG]
 */
@Data
@TableName("ZHKS_FEI_YONG")
public class KaiShiFeiYong {

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
     * 合计
     */
    @TableField(value = "\"合计\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 合计;

    /**
     * 制造费用
     */
    @TableField(value = "\"制造费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 制造费用;

    /**
     * 管理费用
     */
    @TableField(value = "\"管理费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 管理费用;

    /**
     * 财务费用
     */
    @TableField(value = "\"财务费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 财务费用;

    /**
     * 销售费用
     */
    @TableField(value = "\"销售费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 销售费用;

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
        return "KaiShiFeiYong";
    }

    @TableField(exist = false)
    public Boolean isBold;

    /**
     * 配置
     */
    @TableField(exist = false)
    public PeiZhi peiZhi = new PeiZhi();

    //#endregion
}
