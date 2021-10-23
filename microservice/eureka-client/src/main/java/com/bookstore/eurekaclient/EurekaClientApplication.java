package com.bookstore.eurekaclient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
        return "test gateway";
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
