package com.hzsh.util;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;


/**
 * Bootstrap-table 的分页对象模型
 */
@Data
public class BootstrapTablePageDTO<T> {

    /**
     * 数据总数
     */
    public Long total;

    /**
     * 起始位置
     */
    public Long offset;

    /**
     * 页大小
     */
    public Long limit;

    /**
     * 当前数据
     */
    public List<T> rows;

    /**
     * 搜索框填入的文本
     */
    public String search;

    /**
     * 其他搜索内容
     */
    public String otherSearch;

    /**
     * 配置管理页面使用的数据测试日期
     */
    public String day;


    public IPage<T> createPageObject() {
        IPage<T> page = new Page<T>();
        page.setSize(limit == null ? Integer.MAX_VALUE : limit);
        page.setCurrent(offset == null ? 1 : offset / limit + 1);
        return page;
    }

}
