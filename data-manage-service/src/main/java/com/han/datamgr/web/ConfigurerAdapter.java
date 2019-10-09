package com.han.datamgr.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Configuration
public class ConfigurerAdapter implements WebMvcConfigurer {

    //设置排除路径，spring boot 2.*，注意排除掉静态资源的路径，不然静态资源无法访问
    private final String[] excludePath = {"/list"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 添加日志的拦截器
         */
        registry.addInterceptor(new WebRequestEventInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
    }
}