package com.hzsh.hzsh.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    /**
     * <p><b>自定义错误处理的Controller类</b></p>
     *
     * @Description 自定义错误处理的Controller类</ p>
     */
    @RequestMapping(value = "/error/{code}")
    public String error(@PathVariable int code, Model model) {
        String pager = "";
        switch (code) {
            case 403:
                model.addAttribute("code", 403);
                pager = "/error/page";
                break;
            case 404:
                model.addAttribute("code", 404);
                pager = "/error/page";
                break;
            case 500:
                model.addAttribute("code", 500);
                pager = "/error/page";
                break;
        }
        return pager;
    }


}
