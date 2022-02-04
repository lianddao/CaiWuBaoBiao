package com.hzsh.common.utils;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;


public class SupperController<S extends IService, T> {


    @Autowired
    protected S service;


}
