package com.hzsh.Quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hzsh.Quartz.entity.DingShi;
import com.hzsh.Quartz.service.DingShiService;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.GongShi;
import com.hzsh.hzsh.service.GongShiService;
import com.hzsh.util.BootstrapTablePageDTO;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 效益测算 - 定时任务
 */
@Controller
@RequestMapping("/DingShi")
public class DingShiController extends SupperController<DingShiService, DingShi> {


    @GetMapping("/list")
    public String defaultListView(Model model) {
        String 昨天 = DateTime.now().plusDays(- 1).toString("yyyy-MM-dd");
        model.addAttribute("day", 昨天);

        return "quartz/list2";
    }



    /**
     * 默认数据 - data
     */
    @GetMapping("/data")
    @ResponseBody
    public BootstrapTablePageDTO<DingShi> defaultListData(BootstrapTablePageDTO<DingShi> dto) {
        IPage<DingShi> page = dto.createPageObject();

        // 定义排序
        QueryWrapper<DingShi> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(DingShi::getSort);

        // 搜索关键字
        String 搜索关键字 = dto.search;
        queryWrapper.lambda().like(DingShi::getName, 搜索关键字).or().like(DingShi::getProjectName, 搜索关键字);

        service.page(page, queryWrapper);
        dto.total = page.getTotal();
        dto.rows = page.getRecords();

        return dto;
    }



    /**
     * 保存表格修改
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public int edit(String name, String value, String pk) {
        return service.edit(name, value, pk);
    }



    @GetMapping(value = "test")
    @ResponseBody
    public int test() {
        service.执行效益测算的默认定时任务();
        return 1;
    }


    @GetMapping(value = "copyDiGao")
    @ResponseBody
    public int 测试用_复制底稿数据到当前日期() {
        return service.测试用_复制底稿数据到当前日期();
    }


    @GetMapping(value = "removeCopy")
    @ResponseBody
    public int 测试用_删除复制的当前日期的底稿数据() {
        return service.测试用_删除复制的当前日期的底稿数据();
    }






    @GetMapping(value = "run")
    @ResponseBody
    public String run(String day) {
        if (day.length() == 0) {
            return "日期无效";
        }
        try {
            DateTime.parse(day);
        } catch (IllegalArgumentException illegalArgumentException) {
            return "日期无效";
        }

        if (day.equals("2020-09-01")) {
            return "暂不允许覆盖底稿数据";
        }

        service.执行效益测算的默认定时任务(day);

        return "已完成";
    }


}
