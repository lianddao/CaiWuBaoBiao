package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


/**
 * 公式表 [HZSH_GONG_SHI]
 */
@Data
@TableName("HZSH_GONG_SHI")
public class GongShi {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    public String code;

    @TableField("\"表\"")
    public String 表;

    @TableField("\"行\"")
    public String 行;

    @TableField("\"列\"")
    public String 列;

    @TableField("\"单元格\"")
    public String 单元格;

    @TableField("\"单元格公式\"")
    public String 单元格公式;

    @TableField("\"单元格公式直观值\"")
    public String 单元格公式直观值;

    /**
     * 为公式起的名称
     */
    public String name;

    /**
     * 排序
     */
    public Integer sort;


    @TableField(exist = false)
    public Integer paixu;


    /**
     * 价格表临时用测试公式
     */
    @TableField("\"测试用公式\"")
    public String 测试用公式;



    @TableField("\"公式的项目完整体\"")
    public String 公式的项目完整体;




    //#region 扩展
    public String getClassName() {
        return "GongShi";
    }


    /**
     * 配置
     */
    @TableField(exist = false)
    public PeiZhi peiZhi = new PeiZhi();


    public String getDisplayText() {
        return "「" + 表 + " . " + 行 + "  " + code + " . " + 列 + "」";
    }


    @TableField(exist = false)
    public Object 列的值;

    @TableField(exist = false)
    public Object 底稿值;
    //#endregion
}
