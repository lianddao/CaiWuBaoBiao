package com.hzsh.hzsh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.model.BanChengPin;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 半成品 - [HZSH_BAN_CHENG_PIN]
 */
public interface BanChengPinService extends IService<BanChengPin> {

    /**
     * 从Excel导入数据
     */
    @Transactional
    List<BanChengPin> importBanChengPin(MultipartFile file);

    /**
     * 指定日期的数据
     */
    List<BanChengPin> defaultList(String day);

    /**
     * 获取唯一对象
     */
    BanChengPin getUnique(String day, String code);

    /**
     * 修改
     */
    int edit(String name, String value, Long pk);


    int 复制最近日期的数据(String 待保存日期);


    int 测试用_复制底稿数据到今天();


    int 测试用_删除复制的当前日期的底稿数据();


    HSSFSheet 返回导数模板(HSSFWorkbook workbook);


    int 从导数模板导入数据(MultipartFile file);
}
