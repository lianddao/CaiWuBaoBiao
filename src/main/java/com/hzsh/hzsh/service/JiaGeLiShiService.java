package com.hzsh.hzsh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.dto.YuanCaiLiaoHuiLvDTO;
import com.hzsh.hzsh.model.JiaGe;
import com.hzsh.hzsh.model.JiaGeLiShi;
import com.hzsh.hzsh.model.LiRun;

import java.util.List;


/**
 * 价格历史数据 - [JIA_GE_LISHI]
 */
public interface JiaGeLiShiService extends IService<JiaGeLiShi> {

    /**
     * 指定日期的数据
     */
    List<JiaGeLiShi> defaultList(String day);

    /**
     * 获取唯一对象
     */
    JiaGeLiShi getUnique(String day, String priceCode);

    /**
     * 修改
     */
    int edit(String name, String value, Long pk);

    boolean insertOrUpdate(JiaGeLiShi one);



    YuanCaiLiaoHuiLvDTO 原材料表页面展示的汇率数据(String day);

}
