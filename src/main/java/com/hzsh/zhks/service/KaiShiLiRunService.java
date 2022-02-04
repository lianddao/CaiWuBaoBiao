package com.hzsh.zhks.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.zhks.model.KaiShiBanChengPin;
import com.hzsh.zhks.model.KaiShiLiRun;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 中海开氏-利润   [ZHKS_LI_RUN]
 */
public interface KaiShiLiRunService extends IService<KaiShiLiRun> {

    /**
     * 从Excel导入数据
     */
    @Transactional
    List<KaiShiLiRun> importKaiShiLiRun(MultipartFile file);


    /**
     * 指定日期的数据
     */
    List<KaiShiLiRun> defaultList(String month);


    /**
     * 获取唯一对象
     */
    KaiShiLiRun getUnique(String month, String code);


    /**
     * 返回导数模板
     */
    HSSFSheet 返回导数模板(HSSFWorkbook workbook);


    /**
     * 从导数模板导入数据
     */
    int 从导数模板导入数据(MultipartFile file);


    boolean insertOrUpdate(KaiShiLiRun one);

}
