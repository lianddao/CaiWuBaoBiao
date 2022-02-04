package com.hzsh.Quartz.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hzsh.Quartz.entity.QuartzDeploy;
/**
 * 
 * @ClassName:  QuartzDeployMapper   
 * @Description:   
 * @author: 吴国达
 * @date:   2019年5月10日 上午9:48:41
 */
@Mapper
public interface QuartzDeployMapper {
    int deleteByPrimaryKey(String id);

    int insert(QuartzDeploy record);

    int insertSelective(QuartzDeploy record);
   
    QuartzDeploy selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QuartzDeploy record);

    int updateByPrimaryKey(QuartzDeploy record);
    
    List<QuartzDeploy> selectstatustrue();
    
    List<QuartzDeploy> selectByname(@Param("searchname")String searchname);
    
    List<QuartzDeploy> selectBySearchMap(Map<String, Object> searchList);

	int getTotalByBySearchMap(Map<String, Object> searchList);
	
	//条件查询
	List<QuartzDeploy> selectByQuartzDeploy(QuartzDeploy record);

}