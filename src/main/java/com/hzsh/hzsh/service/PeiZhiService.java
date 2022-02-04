package com.hzsh.hzsh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.model.PeiZhi;

import java.util.List;


/**
 * <h1>编码配置 [HZSH_PEI_ZHI]</h1>
 */
public interface PeiZhiService extends IService<PeiZhi> {


    /**
     * 从Excel导入数据时,新增或修改到 [HZSH_PEI_ZHI]
     */
    List<PeiZhi> autoInsertOrUpdate(Object data_list);

    /**
     * 获取唯一对象
     */
    PeiZhi getUnique(String code);

    /**
     * 从excel底稿导入时,根据目录和名称判断唯一性,(sort, isHidden用来处理名称相同的项)
     */
    PeiZhi getUnique(String category, String name, Integer sort, Boolean isHidden);

    /**
     * 获取目录数据
     */
    List<String> getCategoryList();



    int edit(String name, String value, Long pk);


    /**
     * 获取指定目录的配置
     */
    List<PeiZhi> getByCategory(String category);


    /**
     * 新增时, 准备一个编码
     */
    int getAutoCode(String category);


    /**
     * 新增一个
     */
    PeiZhi insertOne(PeiZhi one);

}
