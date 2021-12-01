package com.bookstore.eurekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class RedisSessionInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisSessionInterceptor(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if(username != null){
            String loginSessionId = redisTemplate.opsForValue().get(String.format("username:%s", username));
            if(loginSessionId != null && loginSessionId.equals(session.getId())){
                // success
                return true;
            }else if(loginSessionId != null && !loginSessionId.equals(session.getId())){
                log.info("id mismatch, session invalidate: {}", session.getId());
            }
        }else{
            log.info("please login");
        }
        response401(response);
        return false;
    }

    private void response401(HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print("401");
    }

}
