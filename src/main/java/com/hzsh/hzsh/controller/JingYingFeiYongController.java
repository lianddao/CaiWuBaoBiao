package com.hzsh.hzsh.controller;

import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.JingYingFeiYong;
import com.hzsh.hzsh.service.JingYingFeiYongService;
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



/**
 * 经营费用表 - [HZSH_JINGYING_FEIYONG]
 */
@Controller
@RequestMapping("/JingYingFeiYong")
public class JingYingFeiYongController extends SupperController<JingYingFeiYongService, JingYingFeiYong> {

    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<JingYingFeiYong> dto = PageDTO.createInstance(day);

        model.addAttribute("page_dto", dto);

        return "hzsh/jingying_feiyong/list";
    }




    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<JingYingFeiYong> defaultListData(NoPageDTO dto) {
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
