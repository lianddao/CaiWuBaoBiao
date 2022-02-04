package com.hzsh.Quartz.controller;


import com.hzsh.Quartz.entity.QuartzDeploy;
import com.hzsh.Quartz.service.QuartzDeployService;
import com.hzsh.Quartz.service.QuartzSchedulers;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/QuartzDeploy")
public class QuartzDeployController {


    @Autowired
    private QuartzSchedulers quartzScheduler;

    @Autowired
    private QuartzDeployService quartzDeployService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    QuartzDeploy quartzDeploy1;




    @PostMapping("updateState")
    @ResponseBody
    public int updateState(String id, int state) {
        QuartzDeploy one = quartzDeployService.findById(id);

        // 1. 更新到数据库
        one.setStatus(state + "");
        int result = quartzDeployService.update(one);

        // 2. 改变任务状态
        try {
            if (result == 1) {
                if (state == 0) {
                    quartzScheduler.stopJob(one);
                }
                else {
                    quartzScheduler.startJob(one);
                }
            }
        } catch (ClassNotFoundException classNotFoundException) {
            // 忽略
        } catch (SchedulerException e) {
            // 忽略
        } catch (Exception e) {
            // 忽略
        }

        return result;
    }



    @PostMapping("delete")
    @ResponseBody
    public int delete(String id) throws SchedulerException {
        QuartzDeploy one = quartzDeployService.findById(id);

        // 1. 从数据库中删除
        int result = quartzDeployService.delect(id);

        // 2. 从定时任务中删除
        if (result == 1) {
            try {
                quartzScheduler.deleteJob(one);
            } catch (SchedulerException e) {
                // 忽略
            } catch (Exception e) {
                // 忽略
            }
        }

        return result;
    }






    //#region 旧代码

    /**
     * 停、启定时任务
     *
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/setStatus/{id}/{toStatus}")
    public String setStatus(@PathVariable(name = "id") String id, @PathVariable(name = "toStatus") String toStatus,
                            Model model) throws Exception {
        QuartzDeploy quartzDeploy = new QuartzDeploy();
        int suc = 0;
        String message1 = "";
        String message2 = "";
        try {
            // 1.设置数据库信息
            quartzDeploy = quartzDeployService.findById(id);
            quartzDeploy.setStatus(toStatus);// 设置状态status
            suc = quartzDeployService.update(quartzDeploy);// 更新
            if (suc == 1) {
                message1 = "任务状态设置成功";
                message2 = "setstatussuccess";
            }
            else {
                message1 = "任务启动失败";
                message2 = "setstatusfailure";
            }
            // 2.改变正在运行的定时任务状态
            if ("0".equals(toStatus)) {// 2.1如果运行中，则停止
                quartzScheduler.stopJob(quartzDeploy);
            }
            else if ("1".equals(toStatus)) {// 2.2如果 停止，则运行
                quartzScheduler.startJob(quartzDeploy);
            }
        } catch (Exception e) {
            message1 = e.getMessage().substring(0, 400);
            e.printStackTrace();
        }


        return "redirect:/QuartzDeploy/list?message=" + message2;
    }

    /**
     * 新增定时任务查看页
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/add")
    public String addView(Model model) throws Exception {
        model.addAttribute("quartzDeploy", new QuartzDeploy());
        return "quartz/add";

    }

    /**
     * 新增定时任务数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    public String save(@ModelAttribute QuartzDeploy quartzDeploy) throws Exception {
        int suc = 0;
        String message = "";
        String message1 = "";
        Date now = new Date();

        quartzDeploy.setProjectcode(quartzDeploy1.getProjectcode());
        quartzDeploy.setProjectname(quartzDeploy1.getProjectname());

        try {
            quartzDeploy.setCreatetime(now);//设置创建时间
            //quartzDeploy.setStatus("0");//默认为关闭状态
            if (quartzDeploy.getBigentime() == null) {
                quartzDeploy.setBigentime(now);
            }
            suc = quartzDeployService.save(quartzDeploy);
            if (suc == 1) {
                quartzScheduler.startJob(quartzDeploy);
                message = "savesuccess";
                message1 = "新增成功";
            }
            else {
                message = "savefailure";
                message1 = "新增" + quartzDeploy.getName() + "失败";
            }
        } catch (Exception e) {
            message = e.getMessage();
            message1 = e.getMessage();
            e.printStackTrace();
        }




        return "redirect:/QuartzDeploy/list?message=" + message;

    }

    /**
     * 修改定时任务查看页
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/updateview/{id}")
    public String updateview(@PathVariable(name = "id") String id, Model model) throws Exception {

        QuartzDeploy quartzDeploy = new QuartzDeploy();

        quartzDeploy = quartzDeployService.findById(id);

        model.addAttribute("quartzDeploy", quartzDeploy);
        return "quartz/updateview";
    }

    /**
     * 修改定时任务数据
     *
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public String upate(@ModelAttribute QuartzDeploy quartzDeploy, Model model) throws Exception {
        int suc = 0;
        String message2 = "";
        String message1 = "";

        quartzDeploy.setProjectcode(quartzDeploy1.getProjectcode());
        quartzDeploy.setProjectname(quartzDeploy1.getProjectname());
        try {
            quartzDeploy.setUpdatetime(new Date());//设置更新时间
            suc = quartzDeployService.update(quartzDeploy);
            if (suc == 1) {
                message2 = "updatesuccess";
                if (quartzDeploy.getStatus().equals("1")) {


                    quartzScheduler.deleteJob(quartzDeploy);
                    quartzScheduler.startJob(quartzDeploy);
                }
                message1 = "修改:" + quartzDeploy.getName() + "成功";
            }
            else {
                message2 = "updatefailure";
                message1 = "修改:" + quartzDeploy.getName() + "失败";
            }
        } catch (Exception e) {

            message1 = e.getMessage();
            e.printStackTrace();
        }


        return "redirect:/QuartzDeploy/list?message=" + message2;

    }

    /**
     * 删除定时任务
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/delect/{id}")
    public String delect(Model model, @PathVariable String id) throws Exception {
        int suc = 0;
        String message2 = "";
        String message1 = "";
        try {
            //1.从定时计划中移除
            QuartzDeploy quartzDeploy = this.quartzDeployService.findById(id);

            //2.删除数据库记录
            suc = this.quartzDeployService.delect(id);
            if (suc == 1) {
                message2 = "delectsuccess";
                quartzScheduler.deleteJob(quartzDeploy);
                message1 = "删除id:" + id + "成功";
            }
            else {
                message2 = "delectfailure";
                message1 = "删除id:" + id + "失败";
            }
        } catch (Exception e) {
            message2 = e.getMessage();
            message1 = e.getMessage();
            e.printStackTrace();
        }



        return "redirect:/QuartzDeploy/list?message=" + message2;
    }

    /**
     * 所有定时任务列表页，以id降序
     *
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    public String listAll(Model model, Map<String, Object> searchList) throws Exception {

        String dec = " id ASC";
        int size = 20;
        int pageNum = 1;
        String message = "";
        int TotalPages = 1;
        int allnum = 0;
        if (request.getParameter("pageNum") == null || "".equals(request.getParameter("pageNum"))) {
        }
        else {
            pageNum = Integer.parseInt(searchList.get("pageNum").toString());
        }

        searchList.put("pageNumber", (pageNum - 1) * size + 1);
        searchList.put("pageSize", String.valueOf(pageNum * size));
        searchList.put("order", dec);
        message = request.getParameter("message");
        searchList.remove("pageNum");
        List<QuartzDeploy> quartzDeployList = quartzDeployService.selectBySearchMap(searchList);
        allnum = quartzDeployService.getTotalByBySearchMap(searchList);// 总记录数

        if (allnum > 0) {
            TotalPages = (allnum + size - 1) / size;
        }
        model.addAttribute("searchList", searchList);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("size", size);
        model.addAttribute("TotalPages", TotalPages);
        model.addAttribute("allnum", allnum);
        model.addAttribute("message", message);
        model.addAttribute("quartzDeployList", quartzDeployList);

        return "quartz/list";

    }

    //#endregion


}
