package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 半成品 - [HZSH_BAN_CHENG_PIN]
 */
@Data
@TableName("HZSH_BAN_CHENG_PIN")
public class BanChengPin {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * 数据所属的日期.作为数据唯一性区别
     */
    public Date day;

    /**
     * 编码
     */
    public String code;

    /**
     * 半成品的名称
     */
    public String name;

    /**
     * 对应产成品的名称
     */
    @TableField(value = "\"ChengPinName\"")
    public String chengPinName;


    //#region 1. 期初库存
    /**
     * 期初库存 - 数量(万吨)
     */
    @TableField(value = "\"期初库存数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期初库存数量;

    /**
     * 期初库存 - 单价(元/吨)
     */
    @TableField(value = "\"期初库存单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期初库存单价;

    /**
     * 期初库存 - 金额(万元)
     */
    @TableField(value = "\"期初库存金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期初库存金额;
    //#endregion


    //#region 2. 期末库存
    /**
     * 期末库存 - 数量(万吨)
     */
    @TableField(value = "\"期末库存数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期末库存数量;

    /**
     * 期末库存 - 单价(元/吨)
     */
    @TableField(value = "\"期末库存单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期末库存单价;

    /**
     * 期末库存 - 金额(万元)
     */
    @TableField(value = "\"期末库存金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 期末库存金额;
    //#endregion

    /**
     * 库存减少数量(万吨)
     */
    @TableField(value = "\"库存减少数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 库存减少数量;

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
        return "BanChengPin";
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
