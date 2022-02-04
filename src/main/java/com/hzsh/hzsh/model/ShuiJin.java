package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 税金 - HZSH_SHUIJIN
 */
@Data
@TableName("HZSH_SHUIJIN")
public class ShuiJin {

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
     * 税金项目的名称
     */
    public String name;

    /**
     * ★ 填入的序号
     */
    @TableField(value = "XUHAO")
    public Integer xuhao;

    /**
     * 税率
     */
    @TableField(value = "税率", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 税率;

    /**
     * 基数
     */
    @TableField(value = "基数", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 基数;

    /**
     * 应交税金 = 税率 × 基数
     */
    @TableField(value = "应交税金", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 应交税金;

    /**
     * 备注
     */
    public String remark;

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
        return "ShuiJin";
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