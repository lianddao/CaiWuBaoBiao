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
 * 中海开氏-销售收入表  [ZHKS_XIAOSHOU_SHOURU]
 */
@Data
@TableName("ZHKS_XIAOSHOU_SHOURU")
public class KaiShiXiaoShouShouRu {

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
     * 消费税率
     */
    @TableField(value = "\"消费税率\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 消费税率;

    /**
     * 本月销量
     */
    @TableField(value = "\"本月销量\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 本月销量;

    /**
     * 含税价
     */
    @TableField(value = "\"含税价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 含税价;

    /**
     * 不含税价
     */
    @TableField(value = "\"不含税价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 不含税价;

    /**
     * 收入
     */
    @TableField(value = "\"收入\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 收入;

    /**
     * 裸税价
     */
    @TableField(value = "\"裸税价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 裸税价;



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
        return "KaiShiXiaoShouShouRu";
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
