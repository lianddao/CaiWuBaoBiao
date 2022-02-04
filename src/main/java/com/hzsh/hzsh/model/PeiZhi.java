package com.hzsh.hzsh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;


/**
 * <h1>编码配置 [HZSH_PEI_ZHI]</h1>
 */
@Data
@TableName("HZSH_PEI_ZHI")
public class PeiZhi {

    @TableId(type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;


    /**
     * 表格所属名称. 如 经营费用表, 原材料表
     */
    public String category;

    /**
     * 名称. 如 一、增值税, （一）进口原油
     */
    public String name;

    /**
     * 编码
     */
    public String code;

    /**
     * Excel中的行索引
     */
    public Integer sort;

    /**
     * 层次或缩进
     */
    @TableField("CENG_CI")
    public Integer cengCi;

    /**
     * 是否加粗显示
     */
    public Boolean isBold = false;

    /**
     * 是否只读(如'合计')
     */
    public Boolean isReadonly = false;

    /**
     * 是否隐藏(如高度为0的行)
     */
    public Boolean isHidden = false;






    //#region 扩展


    @TableField(exist = false)
    public List<GongShi> 公式列表;


    @JsonIgnore
    public String getClassName() {
        return "PeiZhi";
    }






    public static int CATEGORY_利润表 = 11;
    public static int CATEGORY_销售收入 = 12;
    public static int CATEGORY_销售成本 = 13;
    public static int CATEGORY_经营费用表 = 14;
    public static int CATEGORY_税金及附加 = 15;
    public static int CATEGORY_原材料表 = 16;
    public static int CATEGORY_半成品 = 17;
    public static int CATEGORY_费用明细 = 18;
    public static int CATEGORY_开氏_利润表 = 21;
    public static int CATEGORY_开氏_销售收入 = 22;
    public static int CATEGORY_开氏_产品成本 = 23;
    public static int CATEGORY_开氏_生产经营 = 24;
    public static int CATEGORY_开氏_费用 = 25;
    public static int CATEGORY_开氏_半成本 = 26;
    //#endregion
}
