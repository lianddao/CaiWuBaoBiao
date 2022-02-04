package com.hzsh.hzsh.dto;

import lombok.Data;


/**
 * (供页面创建公式使用的)公式体中的列
 */
@Data
public class GongShiColumnDTO {

    /**
     * 列的中文名称
     */
    public String lieMing;

    /**
     * 列的实际名称
     */
    public String colName;

    /**
     * 不需要的列
     */
    public boolean hidden;
}
