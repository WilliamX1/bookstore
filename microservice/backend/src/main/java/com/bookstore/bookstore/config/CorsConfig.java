package com.bookstore.bookstore.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * AJAX请求跨域
 *
 * @author DongD
 * @time 2021-05-13
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    //指定允许跨域的多个域
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //添加跨域的cors配置
        registry.addMapping("/**").   //可以被跨域的路径,/**表示无限制,//允许跨域的域名，如果值为*,则表示允许任何域名使用
                allowedOriginPatterns("*").
                allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE"). //允许任何方法，值可以为： "GET", "POST" ...
                allowedHeaders("*"). //允许任何请求头
                allowCredentials(true). //允许带cookie信息
                maxAge(3600); //maxAge(3600):表示3600秒内，不需要再发送预检验请求，是结果可以缓存的时长
    }
}
