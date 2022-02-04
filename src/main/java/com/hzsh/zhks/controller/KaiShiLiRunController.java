package com.hzsh.zhks.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.YuanCaiLiao;
import com.hzsh.util.PageDTO;
import com.hzsh.zhks.model.KaiShiLiRun;
import com.hzsh.zhks.service.KaiShiLiRunService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 中海开氏-利润   [ZHKS_LI_RUN]
 */
@Controller
@RequestMapping("/KaiShiLiRun")
public class KaiShiLiRunController extends SupperController<KaiShiLiRunService, KaiShiLiRun> {


    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String month) {
        month = StringUtils.isEmpty(month) ? new DateTime().toString("yyyy-MM") : month;

        List<KaiShiLiRun> list = service.defaultList(month);

        PageDTO<KaiShiLiRun> dto = new PageDTO<>(list, month);
        model.addAttribute("page_dto", dto);

        return "zhks/li_run/list";
    }



    /**
     * <h2 style="color:red">从Excel导入数据</h2>
     */
    @PostMapping(value = "importKaiShiLiRun")
    @ResponseBody
    public List<KaiShiLiRun> importKaiShiLiRun(MultipartFile file) {
        List<KaiShiLiRun> list = service.importKaiShiLiRun(file);
        return list;
    }



    /**
     * 保存表格修改
     */
    @PostMapping(value = "edit")
    @ResponseBody
    public int edit(String name, String value, Long pk) {
        // 如果清空了值,则应该进行清空

        Map map = new HashMap();
        map.put(name, value);
        KaiShiLiRun one = JSON.parseObject(JSON.toJSONString(map), KaiShiLiRun.class);

        KaiShiLiRun dbOne = service.getById(pk);

        one.id = dbOne.id;
        service.updateById(one);

        return 1;
    }






}
