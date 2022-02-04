package com.hzsh.zhks.controller;

import com.hzsh.common.utils.SupperController;
import com.hzsh.util.PageDTO;
import com.hzsh.zhks.model.KaiShiChanPinChengBen;
import com.hzsh.zhks.service.KaiShiChanPinChengBenService;
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
 * 中海开氏-产品成本  [ZHKS_CHANPIN_CHENGBEN]
 */
@Controller
@RequestMapping("/KaiShiChanPinChengBen")
public class KaiShiChanPinChengBenController extends SupperController<KaiShiChanPinChengBenService, KaiShiChanPinChengBen> {

    /**
     * 默认页面 - list
     */
    @GetMapping("/list")
    public String defaultListView(Model model, String month) {
        month = StringUtils.isEmpty(month) ? new DateTime().toString("yyyy-MM") : month;

        List<KaiShiChanPinChengBen> list = service.defaultList(month);

        PageDTO<KaiShiChanPinChengBen> dto = new PageDTO<>(list, month);
        model.addAttribute("page_dto", dto);

        return "zhks/chanpin_chengben/list";
    }



    /**
     * <h2 style="color:red">从Excel导入数据</h2>
     */
    @PostMapping(value = "importKaiShiChanPinChengBen")
    @ResponseBody
    public List<KaiShiChanPinChengBen> importKaiShiChanPinChengBen(MultipartFile file) {
        List<KaiShiChanPinChengBen> list = service.importKaiShiChanPinChengBen(file);
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
