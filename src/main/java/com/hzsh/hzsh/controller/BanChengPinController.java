package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.BanChengPin;
import com.hzsh.hzsh.model.BanChengPin;
import com.hzsh.hzsh.model.LiRun;
import com.hzsh.hzsh.model.YuanCaiLiao;
import com.hzsh.hzsh.service.BanChengPinService;
import com.hzsh.util.NoPageDTO;
import com.hzsh.util.PageDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 半成品 - [HZSH_BAN_CHENG_PIN]
 */
@Controller
@RequestMapping("/BanChengPin")
public class BanChengPinController extends SupperController<BanChengPinService, BanChengPin> {


    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<BanChengPin> dto = PageDTO.createInstance(day);

        model.addAttribute("page_dto", dto);

        return "hzsh/ban_cheng_pin/list";
    }





    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<BanChengPin> defaultListData(NoPageDTO dto) {
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
