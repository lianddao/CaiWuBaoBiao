package com.hzsh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

@MapperScan("com.hzsh.*.mapper")
public class CaiWuBaoBiaoApplication {
	
	
    public static void main(String[] args) {
        SpringApplication.run(CaiWuBaoBiaoApplication.class, args);
        System.out.println("财务报表-效益测算 已经启动");
    }

}
