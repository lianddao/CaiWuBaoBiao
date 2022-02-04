package com.hzsh.hzsh.model;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;

import java.util.Date;


/**
 * 价格历史数据 - [JIA_GE_LISHI]
 */
@Data
@TableName("JIA_GE_LISHI")
public class JiaGeLiShi {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    /**
     * 数据所属的日期.作为数据唯一性区别
     */
    public Date day;

    /**
     * 价格系统那边的编码
     */
    public String priceCode;

    /**
     * 名称
     */
    public String name;

    /**
     * 日期对应的含税价
     */
    @Deprecated
    @TableField(value = "\"含税价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 含税价;

    /**
     * 日期对应的裸税价
     */
    @TableField(value = "\"裸税价\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 裸税价;

    /**
     * 日期对应的汇率
     */
    @TableField(value = "\"汇率\"", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 汇率;

    @TableField(value = "\"对应的单元格公式\"")
    public String 对应的单元格公式;





    public String getDayString() {
        if (day == null) {
            return DateTime.now().toString("yyyy-MM-dd");
        }
        else {
            return new DateTime(day).toString("yyyy-MM-dd");
        }
    }



    //#region 原材料表的DTO使用
    @Deprecated
    public Double getHanShuiJia() {
        return 含税价;
    }

    public Double getLuoShuiJia() {
        return 裸税价;
    }

    public Double getHuiLv() {
        return 汇率;
    }
    //#endregion
}
