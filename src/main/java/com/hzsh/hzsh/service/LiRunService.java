package com.hzsh.hzsh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.model.LiRun;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 利润表 [HZSH_LI_RUN]
 */
public interface LiRunService extends IService<LiRun> {

    /**
     * 从Excel底稿导入数据
     */
    @Transactional
    List<LiRun> importLiRun(MultipartFile file);

    /**
     * 指定日期的数据
     */
    List<LiRun> defaultList(String day);

    /**
     * 获取唯一对象
     */
    LiRun getUnique(String day, String code);

    /**
     * 修改
     */
    int edit(String name, String value, Long pk);


    int 复制最近日期的数据(String 待保存日期);


    int 测试用_复制底稿数据到今天();


    int 测试用_删除复制的当前日期的底稿数据();
}
