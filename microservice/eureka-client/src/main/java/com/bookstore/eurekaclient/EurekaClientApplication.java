package com.bookstore.eurekaclient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class EurekaClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @RequestMapping(value = "/test")
    public String test() {
        return "test nginx";
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/session/login")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("username", "xhd");
        redisTemplate.opsForValue().set("username:xhd", session.getId());
        return "login finished";
    }
    @GetMapping("/session/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "logout finished";
    }

    @Value("${server.port}")
    private String port;
    @GetMapping(value = "/session/getInfo")
    public Map<String, String> addSession (HttpServletRequest request){
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        String username = (String) session.getAttribute("username");
        String requestURI = request.getRequestURI();

        Map<String, String> sessionInfoMap = new HashMap<>();
        sessionInfoMap.put("sessionId", sessionId);
        sessionInfoMap.put("username", username);
        sessionInfoMap.put("requestURI", requestURI);
        sessionInfoMap.put("port", port);
        return sessionInfoMap;
    }
}

@RestController
class ServiceInstanceRestController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @PostMapping("/service-instances/{applicationName}")
    public String serviceIn(@PathVariable String applicationName) {
        return "Hello " + applicationName;
    }

    @GetMapping("/service-instances/findAuthorByBookname")
    public String findAuthorByBookname(String bookname) {
        String sql = "SELECT * FROM book WHERE bookname = ?";
        Map map = jdbcTemplate.queryForMap(sql, bookname);
        return map.get("author").toString();
    }

}
