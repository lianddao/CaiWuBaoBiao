package com.hzsh.zhks.controller;

import com.hzsh.common.utils.SupperController;
import com.hzsh.util.PageDTO;
import com.hzsh.zhks.model.KaiShiShengChanJingYin;
import com.hzsh.zhks.service.KaiShiShengChanJingYinService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 中海开氏-生产经营  [ZHKS_SHENGCHAN_JINGYIN]
 */
@Controller
@RequestMapping("/KaiShiShengChanJingYin")
public class KaiShiShengChanJingYinController extends SupperController<KaiShiShengChanJingYinService, KaiShiShengChanJingYin> {

    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String month) {
        month = StringUtils.isEmpty(month) ? new DateTime().toString("yyyy-MM") : month;

        List<KaiShiShengChanJingYin> list = service.defaultList(month);

        PageDTO<KaiShiShengChanJingYin> dto = new PageDTO<>(list, month);
        model.addAttribute("page_dto", dto);

        return "zhks/shengchan_jingying/list";
    }



    /**
     * <h2 style="color:red">从Excel导入数据</h2>
     */
    @PostMapping(value = "importKaiShiShengChanJingYin")
    @ResponseBody
    public List<KaiShiShengChanJingYin> importKaiShiShengChanJingYin(MultipartFile file) {
        List<KaiShiShengChanJingYin> list = service.importKaiShiShengChanJingYin(file);
        return list;
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
