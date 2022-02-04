package com.hzsh.hzsh.dto;


import lombok.Data;

/**
 * Excel中的Cell的公式
 */
@Data
public class CellDTO {

    /**
     * 如 C5,D5
     */
    public String address;

    /**
     * 公式
     */
    public String gongShi;

    /**
     * 列的名称
     */
    public String name;


    public String 第几列;
}
