package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.hzsh.service.PeiZhiService;
import com.hzsh.util.BootstrapTablePageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <h1>编码配置 [HZSH_PEI_ZHI]</h1>
 */
@Controller
@RequestMapping("/PeiZhi")
public class PeiZhiController extends SupperController<PeiZhiService, PeiZhi> {

    @Resource
    private GongShiService gongShiService;

    @Resource
    private JdbcTemplate jdbcTemplate;



    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String category, Boolean isShowAll, String day) {
        // 获取目录数据
        List<String> categoryList = service.getCategoryList();
        model.addAttribute("category_data", categoryList);
        model.addAttribute("category", category);
        model.addAttribute("isShowAll", isShowAll);
        if (category != null && category.indexOf("开氏") != - 1) {
            model.addAttribute("day", StringUtils.isEmpty(day) ? "2021-08-01" : day);
        }
        else {
            model.addAttribute("day", StringUtils.isEmpty(day) ? "2020-09-01" : day);
        }


        return "hzsh/pei_zhi/list";
    }



    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public BootstrapTablePageDTO<PeiZhi> defaultListData(BootstrapTablePageDTO<PeiZhi> dto) {

        //#region 1. 数据查询
        IPage<PeiZhi> page = dto.createPageObject();

        // 定义排序
        QueryWrapper<PeiZhi> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(PeiZhi::getSort);

        // 搜索关键字
        String 搜索关键字 = dto.search;

        // 设置搜索条件
        JSONObject 搜索参数 = JSON.parseObject(dto.otherSearch);
        String 目录 = 搜索参数.getString("category");
        boolean 是否显示全部 = 搜索参数.getBooleanValue("isShowAll");


        if (! StringUtils.isEmpty(目录)) {
            queryWrapper.lambda().eq(PeiZhi::getCategory, 目录);
            //                    .nested(i -> i.like(PeiZhi::getName, 搜索关键字).or().like(PeiZhi::getCode, 搜索关键字));
        }

        if (是否显示全部) {
        }
        else {
            queryWrapper.lambda().eq(PeiZhi::getIsHidden, false);
        }

        queryWrapper.lambda().nested(i -> i.like(PeiZhi::getName, 搜索关键字).or().like(PeiZhi::getCode, 搜索关键字));


        // 获取结果
        service.page(page, queryWrapper);
        dto.total = page.getTotal();
        dto.rows = page.getRecords();
        //#endregion





        //#region 2. "所有目录"
        if (StringUtils.isEmpty(目录)) {
            for (PeiZhi one : dto.rows) {
                List<GongShi> Joon文件配置的列结构 = GongShiCache.getColumnList(one.category).stream().filter(i -> ! i.hidden)
                        .map(column -> {
                            GongShi gongShi = new GongShi();
                            gongShi.表 = one.category;
                            gongShi.行 = one.code;
                            gongShi.列 = column.colName;
                            return gongShi;
                        })
                        .collect(Collectors.toList());


                List<GongShi> 已配置的公式列表 = gongShiService.getGongShiList(one.code);
                List<GongShi> 对应Json文件配置的公式列表 = new ArrayList<>();


                for (GongShi gongShi : Joon文件配置的列结构) {
                    String tableName = GongShiCache.getTableName(gongShi.表);
                    GongShi found = 已配置的公式列表.stream().filter(i -> gongShi.列.equals(i.列)).findFirst().orElse(null);
                    if (found == null) {
                        // 查询底稿单元格对应的值
                        String 查询语句 = String.format("SELECT \"%s\" FROM %s WHERE CODE = %s AND TO_CHAR(DAY,'yyyy-MM-dd') = '%s'", gongShi.列, tableName, one.code, dto.day);
                        Object value;
                        try {
                            value = jdbcTemplate.queryForObject(查询语句, Double.class);
                        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                            value = null;
                        }

                        GongShi temp = new GongShi();
                        temp.表 = gongShi.表;
                        temp.行 = one.name;
                        temp.列 = gongShi.列;
                        temp.列的值 = value;
                        对应Json文件配置的公式列表.add(temp);
                    }
                    else {
                        对应Json文件配置的公式列表.add(found);
                    }
                }
                one.公式列表 = 对应Json文件配置的公式列表;
            }


            return dto;
        }

        //#endregion





        //#region 3. 指定了目录
        // 3.1. 获取「Joon文件配置的列结构」
        List<GongShi> Joon文件配置的列结构 = GongShiCache.getColumnList(目录).stream().filter(i -> ! i.hidden)
                .map(column -> {
                    GongShi gongShi = new GongShi();
                    gongShi.表 = 目录;
                    gongShi.列 = column.colName;
                    return gongShi;
                })
                .collect(Collectors.toList());


        // 3.2. 获取「配置」对应的「公式列表」
        for (PeiZhi one : dto.rows) {
            List<GongShi> 已配置的公式列表 = gongShiService.getGongShiList(one.code);
            List<GongShi> 对应Json文件配置的公式列表 = new ArrayList<>();

            for (GongShi gongShi : Joon文件配置的列结构) {
                String tableName = GongShiCache.getTableName(gongShi.表);
                GongShi found = 已配置的公式列表.stream().filter(i -> gongShi.列.equals(i.列)).findFirst().orElse(null);

                String 查询底稿值语句 = String.format("SELECT \"%s\" FROM %s WHERE CODE = %s AND TO_CHAR(DAY,'yyyy-MM-dd') = '%s'", gongShi.列, tableName, one.code, dto.day);
                Object 底稿值;
                try {
                    底稿值 = jdbcTemplate.queryForObject(查询底稿值语句, Double.class);
                } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                    底稿值 = null;
                }

                if (found == null) {
                    // 没有公式
                    GongShi temp = new GongShi();
                    temp.表 = gongShi.表;
                    temp.行 = one.name;
                    temp.列 = gongShi.列;
                    temp.列的值 = 底稿值;
                    temp.底稿值 = 底稿值;
                    对应Json文件配置的公式列表.add(temp);
                }
                else {
                    // 具有公式
                    found.底稿值 = 底稿值;
                    对应Json文件配置的公式列表.add(found);
                }
            }
            one.公式列表 = 对应Json文件配置的公式列表;
        }
        return dto;
        //#endregion
    }



    /**
     * 保存表格修改
     */
    @PostMapping("edit")
    @ResponseBody
    public int edit(String name, String value, Long pk) {
        // 1
        service.edit(name, value, pk);

        // 2
        GongShiCache.更新公式缓存();

        return 1;
    }


    @GetMapping("getAutoCode")
    @ResponseBody
    public int getAutoCode(String category) {
        return service.getAutoCode(category);
    }


    @PostMapping("insertOne")
    @ResponseBody
    public PeiZhi insertOne(PeiZhi one) {
        return service.insertOne(one);
    }

}
