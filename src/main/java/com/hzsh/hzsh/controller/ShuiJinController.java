package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.LiRun;
import com.hzsh.hzsh.model.ShuiJin;
import com.hzsh.hzsh.model.YuanCaiLiao;
import com.hzsh.hzsh.service.ShuiJinService;
import com.hzsh.util.NoPageDTO;
import com.hzsh.util.PageDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 税金 - HZSH_SHUIJIN
 */
@Controller
@RequestMapping("/ShuiJin")
public class ShuiJinController extends SupperController<ShuiJinService, ShuiJin> {


    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<ShuiJin> dto = PageDTO.createInstance(day);

        model.addAttribute("page_dto", dto);

        return "hzsh/shui_jin/list";
    }




    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<ShuiJin> defaultListData(NoPageDTO dto) {
        dto.rows = service.defaultList(dto.day);
        return dto;
    }




    /**
     * 保存表格修改
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public int edit(String name, String value, Long pk) {
        if (name.equals("remark")) {
            ShuiJin one = service.getById(pk);
            one.remark = value;
            service.updateById(one);
        }
        else {
            service.edit(name, value, pk);
        }

        return 1;
    }





}
