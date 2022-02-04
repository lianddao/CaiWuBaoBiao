package com.hzsh.hzsh.dto;

import com.hzsh.hzsh.controller.GongShiCache;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.hzsh.service.GongShiServiceImpl;
import lombok.Data;

import java.util.List;



/**
 * (供页面创建公式使用的)公式体
 */
@Data
public class GongShiDTO {

    /**
     * 表名称
     */
    public String biaoMing;
    /**
     * 表名称
     */
    public String tableName;

    /**
     * 为公式提供的列
     */
    public List<GongShiColumnDTO> columns;

    /**
     * 行名称
     */
    public String hangMing;
    /**
     * 行名称
     */
    public String rowName;


    /**
     * 列名称
     */
    public String lieMing;
    /**
     * 列名称
     */
    public String colName;

    /**
     * 不需要的列
     */
    public boolean hidden;






    public PeiZhi peiZhi = new PeiZhi();



    public static GongShiDTO getInstance(String 公式体) {
        String[] vs = 公式体.split("#");
        String 表名 = vs[0];
        String 行名 = vs[1];
        String 列名 = vs[2];


        List<GongShiDTO> gongShiDTOList = GongShiCache.getTableList();

        GongShiDTO gongShiDTO = gongShiDTOList.stream().filter(i -> i.biaoMing.equals(表名)).findFirst().get();
        String tableName = gongShiDTO.tableName;
        String code = 行名;
        String columnName = gongShiDTO.columns.stream().filter(i -> i.lieMing.equals(列名)).findFirst().get().colName;

        GongShiDTO result = new GongShiDTO();
        result.biaoMing = gongShiDTO.biaoMing;
        result.tableName = tableName;
        result.rowName = code;
        result.lieMing = 列名;
        result.colName = columnName;

        return result;
    }


    /**
     * 公式的直观文本值
     */
    public String get直观文本() {
        return String.format("%s → %s (%s) → %s", biaoMing, hangMing, rowName, lieMing);
    }


    public String get公式体() {
        return String.format("%s#%s#%s", biaoMing, rowName, colName);
    }

}
