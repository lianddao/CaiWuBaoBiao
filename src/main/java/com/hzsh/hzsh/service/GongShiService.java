package com.hzsh.hzsh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hzsh.hzsh.dto.GongShiDTO;
import com.hzsh.hzsh.model.GongShi;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 公式表 [HZSH_GONG_SHI]
 */
public interface GongShiService extends IService<GongShi> {

    int edit(String name, String value, Long pk);

    /**
     * 获取项目对应的公式列表
     *
     * @param code
     * @return
     */
    List<GongShi> getGongShiList(String code);



    /**
     * @param 公式id
     * @param gong_shi
     * @param day      若为null,则取日期最接近的
     * @return
     */
    Object 计算(Object 公式id, String gong_shi, String day);

    Object IF(Object 公式id, String IF的公式, String day);

    Object SUM(Object 公式id, String SUM的公式, String day);

    Object SUMIF(Object 公式id, String SUMIF的公式, String day);

    Object SUMPRODUCT(Object 公式id, String SUMPRODUCT的公式, String day);



    int 修改单元格数据(String tableNameCN, Object 不同数据表的行数据id, String columnName, String 新值);



    int 开发用_填充关联的对象编码();


    String 返回完整的项目公式体(String 单元格公式);




    //#region 卜祥迎 2021年12月15日

    /**
     * 根据Excel，批量导入 记录
     */
    String importExcel(String fileName, MultipartFile file) throws Exception;
    //#endregion

}
