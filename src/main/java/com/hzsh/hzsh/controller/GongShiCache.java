package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzsh.Quartz.service.DingShiService;
import com.hzsh.common.utils.ApplicationContextProvider;
import com.hzsh.hzsh.dto.GongShiColumnDTO;
import com.hzsh.hzsh.dto.GongShiDTO;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.hzsh.service.PeiZhiService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局使用 resources/templates/gong-shi-config.json
 */
@Data
@Component
public class GongShiCache {


    //# region gong-shi-config.json 相关
    private static final String PATH = "classpath:templates/gong-shi-config.json";
    private static List<GongShiDTO> _tableList = new ArrayList<>();
    private static List<GongShiDTO> _rowList = new ArrayList<>();




    public static String getTableName(String tableNameCN) {
        GongShiDTO one = _tableList.stream().filter(i -> i.biaoMing.equals(tableNameCN)).findFirst().get();
        return one.tableName;
    }


    public static List<GongShiDTO> getTableList() {
        return _tableList;
    }


    public static List<GongShiDTO> getRowList(String biaoMing) {
        if (StringUtils.isEmpty(biaoMing)) {
            // 新增公式页面时, 会到达这里, 然后取第一个作默认数据
            biaoMing = _tableList.get(0).biaoMing;
        }
        final String 表名 = biaoMing;
        List<GongShiDTO> list = _rowList.stream().filter(i -> i.biaoMing.equals(表名)).collect(Collectors.toList());
        return list;
    }


    public static List<GongShiDTO> getColumnList(String biaoMing) {
        if (StringUtils.isEmpty(biaoMing)) {
            // 新增公式页面时, 会到达这里, 然后取第一个作默认数据
            biaoMing = _tableList.get(0).biaoMing;
        }
        final String 表名 = biaoMing;
        GongShiDTO one = _rowList.stream().filter(i -> i.biaoMing.equals(表名)).findFirst().orElse(null);
        if (one == null) {
            List<GongShiDTO> temp = new ArrayList<>();
            return temp;
        }

        List<GongShiColumnDTO> gongShiColumnDTOList = one.columns;
        List<GongShiDTO> columnList = JSON.parseArray(JSON.toJSONString(gongShiColumnDTOList), GongShiDTO.class);

        return columnList;
    }



    @Resource
    private PeiZhiService peiZhiService;



    @PostConstruct
    public void init() {
        // 1.
        JSONArray jsonArray = getJson(PATH).getJSONArray("表");
        _tableList = jsonArray.toJavaList(GongShiDTO.class);


        // 2. 所有配置数据
        LambdaQueryWrapper<PeiZhi> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(PeiZhi::getSort);
        List<PeiZhi> allPeiZhi = peiZhiService.list(qw);


        // 3.
        for (GongShiDTO i : _tableList) {
            List<PeiZhi> peiZhiList = allPeiZhi.stream().filter(peiZhi -> peiZhi.category.equals(i.biaoMing)).collect(Collectors.toList());
            for (PeiZhi j : peiZhiList) {
                GongShiDTO one = new GongShiDTO();
                one.biaoMing = i.biaoMing;
                one.tableName = i.tableName;
                one.hangMing = j.name;
                one.rowName = j.code;
                one.columns = i.columns;
                one.peiZhi = j;
                _rowList.add(one);
            }
        }



        // 3. 所有公式数据
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(GongShi::getSort);
        _allGongShiList = gongShiService.list(queryWrapper);




        System.out.println("缓存「gong-shi-config.json」完成");
    }



    private static JSONObject getJson(String path) {
        File file;
        try {
            file = ResourceUtils.getFile(path);
            String content = new String(Files.readAllBytes(file.toPath()));
            return JSONObject.parseObject(content);
        } catch (Exception e) {
            return null;
        }
    }

    //#endregion






    //#region 数据表 [HZSH_GONG_SHI] 相关
    @Resource
    private GongShiService gongShiService;

    private static List<GongShi> _allGongShiList = new ArrayList<>();

    public static List<GongShi> 获取所有公式数据() {
        return _allGongShiList;
    }


    public static void 更新公式缓存() {
        _tableList.clear();
        _rowList.clear();

        // 1.
        JSONArray jsonArray = getJson(PATH).getJSONArray("表");
        _tableList = jsonArray.toJavaList(GongShiDTO.class);


        // 2. 所有配置数据
        PeiZhiService 配置服务 = ApplicationContextProvider.getBean(PeiZhiService.class);
        LambdaQueryWrapper<PeiZhi> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(PeiZhi::getSort);
        List<PeiZhi> allPeiZhi = 配置服务.list(qw);


        // 3.
        for (GongShiDTO i : _tableList) {
            List<PeiZhi> peiZhiList = allPeiZhi.stream().filter(peiZhi -> peiZhi.category.equals(i.biaoMing)).collect(Collectors.toList());
            for (PeiZhi j : peiZhiList) {
                GongShiDTO one = new GongShiDTO();
                one.biaoMing = i.biaoMing;
                one.tableName = i.tableName;
                one.hangMing = j.name;
                one.rowName = j.code;
                one.columns = i.columns;
                one.peiZhi = j;
                _rowList.add(one);
            }
        }



        // 3. 所有公式数据
        GongShiService 公式服务 = ApplicationContextProvider.getBean(GongShiService.class);
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(GongShi::getSort);
        _allGongShiList = 公式服务.list(queryWrapper);




        System.out.println("更新 公式缓存 完成");
    }

    //#endregion
}
