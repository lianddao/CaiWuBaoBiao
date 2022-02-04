package com.hzsh.hzsh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.model.JiaGe;


/**
 * 价格体系.价格表   [PRICE].[F_SUN_PRICE]
 */
public interface JiaGeService extends IService<JiaGe> {


    /**
     * 获取(有数据的,最接近日期的)对象. 并保存到 [价格历史表]
     */
    JiaGe getLast(String priceCode);

}
