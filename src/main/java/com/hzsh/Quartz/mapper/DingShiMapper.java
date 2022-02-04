package com.hzsh.Quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzsh.Quartz.entity.DingShi;
import org.apache.ibatis.annotations.Mapper;


/**
 * 定时任务
 */
@Mapper
public interface DingShiMapper extends BaseMapper<DingShi> {
}
