package com.hzsh.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzsh.common.utils.R;
import com.hzsh.common.utils.SupperController;


import com.hzsh.system.model.*;
import com.hzsh.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统模板
 */
@Controller
@RequestMapping("/Taxes")
public class MouldController extends SupperController<MouldService, Mould> {

	@Autowired
	private MouldService mouldService;// 系统模板


	@Autowired
	HttpServletRequest request;

	/**
	 * 系统首页二级页面
	 */

	@GetMapping("/sytwoindex")
	public String sytwoindex(@RequestParam(name = "mid") String mid,
			@RequestParam(name = "layoutid",required = false) String layoutid, Model model) throws Exception {



		return "sytwoindex";
	}


	

	@RequestMapping("list")
	@ResponseBody
	public IPage<Mould> getListInfo(HttpServletRequest request) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		IPage<Mould> page = new Page<>();
		String li = request.getParameter("limit") == null ? "20" : request.getParameter("limit");
		String off = request.getParameter("offset") == null ? "1" : request.getParameter("offset");

		page.setCurrent(Long.valueOf(off));// 页数
		page.setSize(Long.valueOf(li));// 条数page.setCurrent(Long.valueOf(off));//页数
		page.setSize(Long.valueOf(li));// 条数
		String search = request.getParameter("search");

		QueryWrapper<Mould> lambdaQuery = new QueryWrapper<>();
		if (search != null && !search.equals("")) {
			lambdaQuery.like("mouldname", search);
		}

		lambdaQuery.orderByAsc("morder,mouldname");
		return mouldService.page(page, lambdaQuery);
	}

	// 获取平台列表
	@RequestMapping("getMouldInfo")
	@ResponseBody
	public List<Mould> getPlatListInfo(HttpServletRequest request) {
		QueryWrapper<Mould> menuplatlambdaQuery = new QueryWrapper<>();
		menuplatlambdaQuery.orderByAsc("morder");
		return service.list(menuplatlambdaQuery);
	}



	@RequestMapping("findById")
	@ResponseBody
	public Mould findById(@RequestParam(name = "id") String id) {
		Mould entity = new Mould();

		entity = service.getById(id);

		return entity;
	}

	
	

}
