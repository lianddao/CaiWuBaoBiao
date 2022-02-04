package com.hzsh.zhks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.zhks.model.KaiShiXiaoShouShouRu;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 中海开氏-销售收入表  [ZHKS_XIAOSHOU_SHOURU]
 */
public interface KaiShiXiaoShouShouRuService extends IService<KaiShiXiaoShouShouRu> {

    /**
     * 从Excel导入数据
     */
    public List<KaiShiXiaoShouShouRu> importKaiShiXiaoShouShouRu(MultipartFile file);


    /**
     * 指定日期的数据
     */
    List<KaiShiXiaoShouShouRu> defaultList(String month);


    /**
     * 获取唯一对象、按照月、名称
     */
    public KaiShiXiaoShouShouRu getUnique(String month, String code);


    int edit(String name, String value, Long pk);


    /**
     * 返回导数模板
     */
    HSSFSheet 返回导数模板(HSSFWorkbook workbook);


    /**
     * 从导数模板导入数据
     */
    int 从导数模板导入数据(MultipartFile file);


    boolean insertOrUpdate(KaiShiXiaoShouShouRu one);
}
