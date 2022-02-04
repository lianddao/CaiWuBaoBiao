package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 销售收入 - [HZSH_XIAOSHOU_SHOURU]
 */
@Data
@TableName("HZSH_XIAOSHOU_SHOURU")
public class XiaoShouShouRu {

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
     * 目的名称
     */
    public String name;

    @TableField(value = "\"本期销售数量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售数量;

    @TableField(value = "\"本期销售单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售单价;

    @TableField(value = "\"本期销售金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本期销售金额;

    @TableField(value = "\"单价含税\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 单价含税;

    @TableField(value = "\"税率\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 税率;

    @TableField(value = "\"消费税标准\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 消费税标准;

    @TableField(value = "\"消费税\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 消费税;

    @TableField(value = "\"裸税单价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 裸税单价;

    /**
     * SAP物料号
     */
    public Integer sapCode;

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
        return "XiaoShouShouru";
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
