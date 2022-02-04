package com.hzsh.hzsh.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.hzsh.common.utils.SupperController;
import com.hzsh.hzsh.model.Actionlogs;

import com.hzsh.hzsh.service.ActionlogsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;


/**
系统操作日志对象
 */
@Controller
@RequestMapping("/Actionlogs")
public class ActionlogsController extends SupperController<ActionlogsService, Actionlogs> {

	@Autowired
	private ActionlogsService actionlogsService;// 系统模板


	@Autowired
	HttpServletRequest request;


	@RequestMapping("list")
	@ResponseBody
	public IPage<Actionlogs> getListInfo(HttpServletRequest request) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		IPage<Actionlogs> page = new Page<>();
		String li = request.getParameter("limit") == null ? "20" : request.getParameter("limit");
		String off = request.getParameter("offset") == null ? "1" : request.getParameter("offset");

		page.setCurrent(Long.valueOf(off));// 页数
		page.setSize(Long.valueOf(li));// 条数page.setCurrent(Long.valueOf(off));//页数
		page.setSize(Long.valueOf(li));// 条数
		String search = request.getParameter("search");

		QueryWrapper<Actionlogs> lambdaQuery = new QueryWrapper<>();
		if (search != null && !search.equals("")) {
			lambdaQuery.like("mouldname", search);
		}

		lambdaQuery.orderByAsc("morder,mouldname");
		return actionlogsService.page(page, lambdaQuery);
	}



	@RequestMapping("findById")
	@ResponseBody
	public Actionlogs findById(@RequestParam(name = "id") String id) {
		Actionlogs entity = new Actionlogs();

		entity = actionlogsService.getById(id);

		return entity;
	}

	
	

}
