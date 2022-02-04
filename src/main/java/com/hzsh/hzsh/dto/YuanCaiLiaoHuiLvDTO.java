package com.hzsh.hzsh.dto;

import com.hzsh.hzsh.model.JiaGeLiShi;
import lombok.Data;

/**
 * 原材料表页面的汇率部分结构
 */
@Data
public class YuanCaiLiaoHuiLvDTO {

    /**
     * 布伦特
     */
    public JiaGeLiShi BuLunTe;

    /**
     * 迪拜
     */
    public JiaGeLiShi DiBai;

    /**
     * OmanDubai
     */
    public JiaGeLiShi OmanDubai;

    /**
     * Oman官价
     */
    public JiaGeLiShi OmanGuanJia;

}
