package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 原材料 - [HZSH_YUAN_CAI_LIAO]
 */
@Data
@TableName("HZSH_YUAN_CAI_LIAO")
public class YuanCaiLiao {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * 数据所属的日期.作为数据唯一性区别
     */
    public Date day;

    /**
     * SAP物料号
     */
    public Integer sapCode;

    /**
     * 编码
     */
    public String code;

    /**
     * 原油品种名称
     */
    public String name;






    /**
     * 本期采购 - 数量(吨)
     */
    @TableField(value = "\"本期采购数量吨\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购数量吨;

    /**
     * 本期采购 - 数量(桶)
     */
    @TableField(value = "\"本期采购数量桶\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购数量桶;

    /**
     * 本期采购 - 价格 - 小结(元/吨)
     */
    @TableField(value = "\"本期采购价格小计\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购价格小计;

    /**
     * 本期采购 - 价格 - 结算价格(或CIF) - 美元/桶
     */
    @TableField(value = "\"本期采购价格美元\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购价格美元;

    /**
     * 本期采购 - 价格 - 结算价格(或CIF) - 元/吨
     */
    @TableField(value = "\"本期采购价格元\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购价格元;

    /**
     * 本期采购 - 价格 - 单位运杂费(元/吨)
     */
    @TableField(value = "\"本期采购单位运杂费\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购单位运杂费;

    /**
     * 本期采购 - 到厂运杂费(万元)
     */
    @TableField(value = "\"本期采购到厂运杂费\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购到厂运杂费;

    /**
     * 本期采购 - 到厂总成本(万元)
     */
    @TableField(value = "\"本期采购到厂总成本\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期采购到厂总成本;






    @TableField(value = "\"本期加工数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期加工数量;

    /**
     * 本期加工 - 单位成本
     */
    @TableField(value = "\"本期加工单位成本\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期加工单位成本;

    /**
     * 本期加工 - 总成本
     */
    @TableField(value = "\"本期加工总成本\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期加工总成本;

    /**
     * 吨桶比
     * <p style='fontWeight:bold; color:red'>业务逻辑上应从某处获取</p>
     * <p style='color:blue'>吨桶比 = 本期采购数量桶 ÷ 本期采购数量吨</p>
     */
    @TableField(value = "\"吨桶比\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 吨桶比;

    //    private void set吨桶比(Double 吨桶比) {
    //        this.吨桶比 = 吨桶比;
    //    }
    //
    //    public Double get吨桶比() {
    //        if (本期采购数量桶 == null) {
    //            return null;
    //        }
    //        if (本期采购数量吨 == null || 本期采购数量吨 == 0) {
    //            return null;
    //        }
    //        return 本期采购数量桶 / 本期采购数量吨;
    //    }


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
        return "YuanCaiLiao";
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
