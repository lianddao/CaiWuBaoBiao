package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.dto.YuanCaiLiaoHuiLvDTO;
import com.hzsh.hzsh.model.JiaGeLiShi;
import com.hzsh.hzsh.model.YuanCaiLiao;
import com.hzsh.hzsh.service.JiaGeLiShiService;
import com.hzsh.hzsh.service.YuanCaiLiaoService;
import com.hzsh.util.NoPageDTO;
import com.hzsh.util.PageDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


/**
 * <h1>原材料 - [HZSH_YUAN_CAI_LIAO]</h1>
 */
@Controller
@RequestMapping("/YuanCaiLiao")
public class YuanCaiLiaoController extends SupperController<YuanCaiLiaoService, YuanCaiLiao> {

    @Resource
    private JiaGeLiShiService jiaGeLiShiService;



    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<YuanCaiLiao> dto1 = PageDTO.createInstance(day);
        model.addAttribute("page_dto", dto1);


        YuanCaiLiaoHuiLvDTO dto2 = jiaGeLiShiService.原材料表页面展示的汇率数据(day);
        model.addAttribute("data2", dto2);

        return "hzsh/yuan_cai_liao/list";
    }




    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<YuanCaiLiao> defaultListData(NoPageDTO dto) {
        dto.rows = service.defaultList(dto.day);
        return dto;
    }




    /**
     * 保存表格修改
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public int edit(String name, String value, Long pk) {
        return service.edit(name, value, pk);
    }


}
