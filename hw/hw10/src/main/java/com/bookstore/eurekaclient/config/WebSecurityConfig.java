package com.bookstore.eurekaclient.config;

import com.bookstore.eurekaclient.RedisSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {
    @Autowired
    RedisSessionInterceptor redisSessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(redisSessionInterceptor).addPathPatterns("/session/**").excludePathPatterns("/session/login");
        super.addInterceptors(registry);
    }
}
