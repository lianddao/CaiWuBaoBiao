package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * 利润表 [HZSH_LI_RUN]
 */
@Data
@TableName("HZSH_LI_RUN")
public class LiRun {

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
     * 项目
     */
    public String name;

    /**
     * 当月测算利润
     */
    @TableField(value = "\"测算\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 测算;

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
        return "LiRun";
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
