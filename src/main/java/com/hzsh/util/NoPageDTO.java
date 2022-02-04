package com.hzsh.util;

import lombok.Data;

import java.util.List;

/**
 * Bootstrap-table 不分页对象模型
 */
@Data
public class NoPageDTO<T> {

    @Deprecated
    public String month;

    public String day;

    /**
     * 当前数据
     */
    public List<T> rows;

}
