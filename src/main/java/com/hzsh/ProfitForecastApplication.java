package com.hzsh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

@MapperScan("com.hzsh.*.mapper")
public class ProfitForecastApplication {
	
	
    public static void main(String[] args) {
        SpringApplication.run(ProfitForecastApplication.class, args);
        System.out.println("工业互联网平台效益测算已经启动");
    }

}
