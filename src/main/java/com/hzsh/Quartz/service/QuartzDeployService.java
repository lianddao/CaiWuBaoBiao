package com.hzsh.Quartz.service;

import java.util.List;
import java.util.Map;
import com.hzsh.Quartz.entity.QuartzDeploy;


public interface QuartzDeployService {


	 public int save(QuartzDeploy quartzDeploy);
	 public int update(QuartzDeploy quartzDeploy);
	 public int delect(String id);
	 public QuartzDeploy findById(String id);
	 public List<QuartzDeploy> selectByname(String searchname);
	 List<QuartzDeploy> selectBySearchMap(Map<String, Object> searchList);

	 int getTotalByBySearchMap(Map<String, Object> searchList);
	 public List<QuartzDeploy>  SelectList(QuartzDeploy quartzDeploy);
	

}