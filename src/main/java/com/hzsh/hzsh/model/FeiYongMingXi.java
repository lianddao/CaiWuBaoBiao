package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 费用明细 - [HZSH_FEI_YONG]
 */
@Data
@TableName("HZSH_FEI_YONG")
public class FeiYongMingXi {

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
     * 名称
     */
    public String name;

    /**
     * 合计
     */
    @TableField(value = "合计")
    public Double 合计;

    /**
     * 生产费用
     */
    @TableField(value = "\"生产费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 生产费用;

    /**
     * 管理费用
     */
    @TableField(value = "\"管理费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 管理费用;

    /**
     * 销售费用
     */
    @TableField(value = "\"销售费用\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 销售费用;


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
        return "FeiYong";
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
