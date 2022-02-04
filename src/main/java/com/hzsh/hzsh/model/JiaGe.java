package com.hzsh.hzsh.model;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 价格体系.价格表   [PRICE].[F_SUN_PRICE]
 * "财务价格计算结果表"
 */
@Data
@TableName("F_SUN_PRICE")
public class JiaGe {


    public String id;

    @TableField(value = "PRICECODE")
    public String priceCode;

    @TableField(value = "PRICENAME")
    public String priceName;

    @TableField(value = "ITEMNAME")
    public String name;

    @TableField(value = "UPDATETIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date day;

    @TableField(value = "CYTAXPRICE", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 含税价;

    @TableField(value = "CYNUDETAXPRICE", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 裸税价;

    /**
     * 美元汇率(1元兑换多少美元)
     */
    @TableField(value = "MOUNTRMB", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DOUBLE)
    public Double 汇率;

}
