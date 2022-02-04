package com.hzsh.zhks.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hzsh.hzsh.model.PeiZhi;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 中海开氏-利润   [ZHKS_LI_RUN]
 */
@Data
@TableName("ZHKS_LI_RUN")
public class KaiShiLiRun {

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
     * 预测金额
     */
    @TableField(value = "\"预测金额\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 预测金额;

    /**
     * 备注
     */
    public String remark;


    /**
     * Excel中的行索引
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
        return "KaiShiLiRun";
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
