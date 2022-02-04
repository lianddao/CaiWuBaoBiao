package com.hzsh.hzsh.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.dto.GongShiDTO;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.PeiZhi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.hzsh.service.PeiZhiService;
import com.hzsh.util.BootstrapTablePageDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



/**
 * 公式表 [HZSH_GONG_SHI]
 */
@Controller
@RequestMapping("/GongShi")
public class GongShiController extends SupperController<GongShiService, GongShi> {

    @Resource
    private PeiZhiService peiZhiService;

    @Resource
    private GongShiService gongShiService;


    /**
     * 公式列表页面 - list
     */
    @Deprecated
    @GetMapping("/list")
    public String defaultListView(Model model, String category) {
        // 获取目录数据
        List<String> categoryList = peiZhiService.getCategoryList();
        model.addAttribute("category_data", categoryList);
        model.addAttribute("category", category);

        return "hzsh/gong_shi/list";
    }


    /**
     * 新增公式页面 - add-gongshi
     */
    @GetMapping("/add")
    public String addView(Model model) {
        return "hzsh/gong_shi/add-gongshi";
    }


    /**
     * 修改公式页面
     */
    @GetMapping("/update")
    public String updateView(Model model, long id) {
        GongShi one = service.getById(id);
        model.addAttribute("gongShi", one);

        return "hzsh/gong_shi/update";
    }



    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public BootstrapTablePageDTO<GongShi> defaultListData(BootstrapTablePageDTO<GongShi> dto) {
        IPage<GongShi> page = dto.createPageObject();

        // 定义排序
        QueryWrapper<GongShi> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(GongShi::get表, GongShi::getSort, GongShi::get列);

        // 设置搜索条件
        String 目录 = dto.otherSearch;
        if (dto.otherSearch.length() == 0) {
            queryWrapper.lambda()
                    .like(GongShi::getName, dto.search)
                    .or()
                    .like(GongShi::get单元格公式直观值, dto.search);
        }
        else {
            queryWrapper.lambda()
                    .eq(GongShi::get表, 目录)
                    .nested(i -> i.like(GongShi::getName, dto.search).or().like(GongShi::get单元格公式直观值, dto.search));
        }


        // 获取结果
        service.page(page, queryWrapper);
        dto.total = page.getTotal();
        dto.rows = page.getRecords();

        // 获取配置信息,显示页面效果
        for (GongShi one : dto.rows) {
            PeiZhi peiZhi = peiZhiService.getUnique(one.code);
            one.peiZhi = peiZhi;
        }

        return dto;
    }






    @GetMapping("/biao")
    @ResponseBody
    public List<GongShiDTO> 表() {
        List<GongShiDTO> tableList = GongShiCache.getTableList();
        return tableList;
    }


    @GetMapping("/hang")
    @ResponseBody
    public List<GongShiDTO> 行(String biaoMing) {
        List<GongShiDTO> rowList = GongShiCache.getRowList(biaoMing);
        return rowList;
    }


    @GetMapping("/lie")
    @ResponseBody
    public List<GongShiDTO> 列(String biaoMing) {
        List<GongShiDTO> columnList = GongShiCache.getColumnList(biaoMing);
        return columnList;
    }


    @GetMapping("jiSuan")
    @ResponseBody
    public Object 计算(String gong_shi_id, String gong_shi, String testDay) {
        if (StringUtils.isEmpty(testDay)) {
            testDay = "2020-09-01";
        }
        if (gong_shi.indexOf("开氏") != - 1) {
            testDay = "2021-08-01";
        }

        // 若包含价格表公式, 且测试日期为 2020-09-01, 使用底稿测试值
        if (gong_shi.contains("价格表#") && testDay.equals("2020-09-01")) {
            if (StringUtils.isEmpty(gong_shi_id)) {
                LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.like(GongShi::get单元格, gong_shi);
                List<GongShi> list = gongShiService.list(queryWrapper);
                if (list.size() > 0) {
                    GongShi one = list.get(0);
                    gong_shi = one.测试用公式;
                }
            }
            else {
                GongShi one = service.getById(gong_shi_id);
                gong_shi = one.测试用公式;
            }
            System.out.println("进入测试条件,使用测试用公式");
        }

        Object obj = service.计算(gong_shi_id, gong_shi, testDay);
        return obj;
    }


    @GetMapping("填充关联的对象编码")
    @ResponseBody
    public int 填充关联的对象编码() {
        service.开发用_填充关联的对象编码();
        return 1;
    }






    @GetMapping("SUM")
    @ResponseBody
    public Object SUM() {
        Object obj = service.SUM(null, null, "2020-09-01");
        return obj;
    }

    @GetMapping("IF")
    @ResponseBody
    public Object IF() {
        Object obj = service.IF(null, null, "2020-09-01");
        return obj;
    }

    @GetMapping("SUMIF")
    @ResponseBody
    public Object SUMIF() {
        Object obj = service.SUMIF(null, null, "2020-09-01");
        return obj;
    }

    @GetMapping("SUMPRODUCT")
    @ResponseBody
    public Object SUMPRODUCT() {
        Object obj = service.SUMPRODUCT(null, null, "2020-09-01");
        return obj;
    }





    /**
     * 新增或修改一个公式
     *
     * @param target      目标单元格的公式体
     * @param target_cn   单元格直观值
     * @param gong_shi    取数的公式体
     * @param gong_shi_cn 公式直观值
     * @return
     */
    @PostMapping("insertOrUpdate")
    @ResponseBody
    public String insertOrUpdate(String target, String target_cn, String gong_shi, String gong_shi_cn) {
        String code = target.split("#")[1];
        String 表 = target_cn.split("#")[0];
        String 行 = target_cn.split("#")[1];
        String 列 = target_cn.split("#")[2];

        // 1. 获取唯一对象
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GongShi::getCode, code).eq(GongShi::get列, 列);
        GongShi dbOne = service.getOne(queryWrapper);

        // 2. 新增或修改
        String result;
        if (dbOne == null) {
            GongShi one = new GongShi();
            one.code = code;
            one.表 = 表;
            one.行 = 行;
            one.列 = 列;
            one.单元格 = target;
            one.单元格公式 = gong_shi;
            one.单元格公式直观值 = gong_shi_cn;
            one.公式的项目完整体 = service.返回完整的项目公式体(gong_shi);

            // 从[PEI_ZHI]表获取排序
            PeiZhi peiZhi = peiZhiService.getUnique(code);
            one.sort = peiZhi.sort;

            boolean ok = service.save(one);
            result = "新增" + (ok ? "成功" : "失败");
        }
        else {
            dbOne.单元格公式 = gong_shi;
            dbOne.单元格公式直观值 = gong_shi_cn;
            dbOne.公式的项目完整体 = service.返回完整的项目公式体(gong_shi);
            boolean ok = service.updateById(dbOne);
            result = "修改" + (ok ? "成功" : "失败");
        }

        // 3.
        GongShiCache.更新公式缓存();


        return result;
    }



    /**
     * 保存表格修改
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public int edit(String name, String value, Long pk) {
        service.edit(name, value, pk);

        GongShiCache.更新公式缓存();

        return 1;
    }



    /**
     * 弹出窗口,修改公式
     */
    @PostMapping(value = "edit2")
    @ResponseBody
    public boolean edit2(Long id, String gong_shi, String gong_shi_text) {
        GongShi one = service.getById(id);
        one.单元格公式 = gong_shi;
        one.单元格公式直观值 = gong_shi_text;
        one.公式的项目完整体 = service.返回完整的项目公式体(one.单元格公式);
        service.updateById(one);

        GongShiCache.更新公式缓存();


        return true;
    }




    /**
     * 删除公式
     */
    @PostMapping(value = "remove")
    @ResponseBody
    public boolean remove(long id) {
        service.removeById(id);

        GongShiCache.更新公式缓存();


        return true;
    }





    //#region 卜祥迎

    /**
     * 返回excel导入界面
     */
    @GetMapping("/importExcelView")
    public String importExcelView(Model model) {
        model.addAttribute("ok", "true");
        return "hzsh/gong_shi/importExcelView";
    }

    @ResponseBody
    @PostMapping(value = "/importExcel")
    public String importExcel(@RequestParam(value = "filename") MultipartFile file) {
        String message = "导入成功";
        String fileName = file.getOriginalFilename();

        try {
            message = gongShiService.importExcel(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        return message;
    }

    //#endregion


}
