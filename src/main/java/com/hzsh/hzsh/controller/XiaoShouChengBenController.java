package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.LiRun;
import com.hzsh.hzsh.model.XiaoShouChengBen;
import com.hzsh.hzsh.model.XiaoShouChengBen;
import com.hzsh.hzsh.service.XiaoShouChengBenService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 销售成本 - HZSH_XIAOSHOU_CHENGBEN
 */
@Controller
@RequestMapping("/XiaoShouChengBen")
public class XiaoShouChengBenController extends SupperController<XiaoShouChengBenService, XiaoShouChengBen> {



    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<XiaoShouChengBen> dto = PageDTO.createInstance(day);

        model.addAttribute("page_dto", dto);

        return "hzsh/xiaoshou_chengben/list";
    }




    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<XiaoShouChengBen> defaultListData(NoPageDTO dto) {
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
