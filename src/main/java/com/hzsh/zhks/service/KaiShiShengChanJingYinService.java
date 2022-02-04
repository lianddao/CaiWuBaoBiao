package com.hzsh.zhks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.zhks.model.KaiShiShengChanJingYin;
import com.hzsh.zhks.model.KaiShiXiaoShouShouRu;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 中海开氏-生产经营  [ZHKS_SHENGCHAN_JINGYIN]
 */
public interface KaiShiShengChanJingYinService extends IService<KaiShiShengChanJingYin> {
    /**
     * 从Excel导入数据
     */
    public List<KaiShiShengChanJingYin> importKaiShiShengChanJingYin(MultipartFile file);


    /**
     * 指定日期的数据
     */
    List<KaiShiShengChanJingYin> defaultList(String month);


    /**
     * 获取唯一对象、按照月、名称
     */
    public KaiShiShengChanJingYin getUnique(String month, String code);


    int edit(String name, String value, Long pk);

    /**
     * 返回导数模板
     */
    HSSFSheet 返回导数模板(HSSFWorkbook workbook);


    /**
     * 从导数模板导入数据
     */
    int 从导数模板导入数据(MultipartFile file);


    boolean insertOrUpdate(KaiShiShengChanJingYin one);
}
