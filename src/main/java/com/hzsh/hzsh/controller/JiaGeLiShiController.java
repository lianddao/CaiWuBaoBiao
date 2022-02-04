package com.hzsh.hzsh.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.model.JiaGeLiShi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.hzsh.service.JiaGeLiShiService;
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
import java.util.*;
import java.util.stream.Collectors;



/**
 * 价格历史数据 - [JIA_GE_LISHI]
 */
@Controller
@RequestMapping("/JiaGeLiShi")
public class JiaGeLiShiController extends SupperController<JiaGeLiShiService, JiaGeLiShi> {


    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String day) {
        day = StringUtils.isEmpty(day) ? new DateTime().plusDays(- 1).toString("yyyy-MM-dd") : day;

        PageDTO<JiaGeLiShi> dto = PageDTO.createInstance(day);

        model.addAttribute("page_dto", dto);

        return "hzsh/jia_ge/list";
    }



    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public NoPageDTO<JiaGeLiShi> defaultListData(NoPageDTO dto) {
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






    @Resource
    private GongShiService gongShiService;

    @GetMapping("/test")
    @ResponseBody
    public int test() {
        LambdaQueryWrapper<GongShi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GongShi::get表, "原材料表").like(GongShi::get单元格公式, "价格表");
        List<GongShi> list1 = gongShiService.list(queryWrapper);


        List<String> list2 = list1.stream().map(i -> i.测试用公式).collect(Collectors.toList());

        List<String> result = new ArrayList<>();
        for (String one : list2) {
            List<String> foundList = Arrays.stream(one.split("\\s+")).filter(i -> i.contains("价格表")).collect(Collectors.toList());
            if (! result.containsAll(foundList)) {
                result.addAll(foundList);
                String json = JSON.toJSONString(foundList);
                System.out.println(json);
            }
        }

        List<String> filterResult = new ArrayList<>();
        for (String one : result) {
            if (! filterResult.contains(one)) {
                filterResult.add(one);
            }
        }

        String json = JSON.toJSONString(filterResult);


        return 1;
    }

}
