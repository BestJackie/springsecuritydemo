package com.example.springsecurity.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * FileName: WebMvcConfiguration
 * Author:   haichaoyang3
 * Date:     2019/9/18 11:34
 * Description:mvc
 * History:
 * since: 1.0.0
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/getUserList").setViewName("userList");
        registry.addViewController("/getOrderList").setViewName("orderList");
        registry.addViewController("/binding").setViewName("binding");
        registry.addViewController("/signup").setViewName("signup");
    }


}
