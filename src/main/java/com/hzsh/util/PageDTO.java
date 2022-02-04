package com.hzsh.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class PageDTO<T> {

    @Deprecated
    public List<T> tableData;

    @Deprecated
    public String month;

    @Deprecated
    public String monthCnString;

    public String day;

    public String dayCnString;

    /**
     * 表格的数据
     */
    @Deprecated
    List<T> rows;


    public static PageDTO createInstance(String day) {
        DateTime dateTime = DateTime.parse(day, DateTimeFormat.forPattern("yyyy-MM-dd"));
        PageDTO one = new PageDTO();
        one.day = dateTime.toString("yyyy-MM-dd");
        one.dayCnString = dateTime.toString("yyyy年MM月dd日");
        return one;
    }

    public PageDTO() {
    }

    public PageDTO(String argMonth) {
        DateTime dateTime = DateTime.parse(argMonth, DateTimeFormat.forPattern("yyyy-MM"));
        month = dateTime.toString("yyyy-MM");
        monthCnString = dateTime.toString("yyyy年MM月");
        day = dateTime.toString("yyyy-MM-01");
        dayCnString = dateTime.toString("yyyy年MM月1日");
    }



    public PageDTO(List<T> table_data, String m) {
        tableData = table_data;
        rows = table_data;

        DateTime dateTime = DateTime.parse(m, DateTimeFormat.forPattern("yyyy-MM"));
        month = dateTime.toString("yyyy-MM");
        monthCnString = dateTime.toString("yyyy年MM月");
        day = dateTime.toString("yyyy-MM-01");
        dayCnString = dateTime.toString("yyyy年MM月1日");
    }


    public PageDTO(List<T> table_data, Date d) {
        tableData = table_data;

        DateTime dateTime = new DateTime(d);
        month = dateTime.toString("yyyy-MM");
        monthCnString = dateTime.toString("yyyy年MM月");
        day = dateTime.toString("yyyy-MM-dd");
        dayCnString = dateTime.toString("yyyy年MM月dd日");
    }




}
