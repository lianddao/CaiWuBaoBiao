package com.hzsh.Quartz.service;

import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hzsh.Quartz.entity.QuartzDeploy;
import com.hzsh.Quartz.mapper.QuartzDeployMapper;

import javax.annotation.Resource;

@Service
public class QuartzDeployServiceImpl implements QuartzDeployService {

    @Autowired
    private QuartzSchedulers quartzSchedulers;
    @Resource
    private QuartzDeployMapper quartzDeployMapper;

    public int save(QuartzDeploy quartzDeploy) {
        int i = 0;
        i = quartzDeployMapper.insert(quartzDeploy);
        if (i == 1) {
            try {
                quartzSchedulers.startAllJob();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }


    public int update(QuartzDeploy quartzDeploy) {
        int i = 0;
        i = quartzDeployMapper.updateByPrimaryKeySelective(quartzDeploy);
        if (i == 1) {
            try {
                quartzSchedulers.startAllJob();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;

    }


    public int delect(String id) {
        int i = 0;
        i = quartzDeployMapper.deleteByPrimaryKey(id);
        if (i == 1) {
            try {
                quartzSchedulers.startAllJob();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;

    }


    public QuartzDeploy findById(String id) {
        return quartzDeployMapper.selectByPrimaryKey(id);
    }

    public List<QuartzDeploy> selectByname(String searchname) {

        return quartzDeployMapper.selectByname(searchname);
    }



    public List<QuartzDeploy> SelectList(QuartzDeploy quartzDeploy) {
        return quartzDeployMapper.selectByQuartzDeploy(quartzDeploy);
    }


    public List<QuartzDeploy> selectBySearchMap(Map<String, Object> searchList) {
        return quartzDeployMapper.selectBySearchMap(searchList);
    }


    public int getTotalByBySearchMap(Map<String, Object> searchList) {
        return quartzDeployMapper.getTotalByBySearchMap(searchList);
    }

}