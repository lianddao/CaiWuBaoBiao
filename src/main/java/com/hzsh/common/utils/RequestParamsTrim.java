package com.hzsh.common.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})  //作用范围
@Retention(RetentionPolicy.RUNTIME)  //生效时期
@Documented  //文档化
public @interface RequestParamsTrim {

}