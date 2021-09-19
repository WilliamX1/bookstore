package com.bookstore.bookstore.controller;

import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.activemq.QueueProducer;
import com.bookstore.bookstore.constant.OrderConstant;
import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    QueueProducer orderProducer = new QueueProducer(OrderConstant.QUEUE_NAME);

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private static final String QUEUE_NAME = "order";

    @GetMapping("/order/testOrder")
    public String test () {
        return "This will reach here";
    }

    @PostMapping("/order/addOrderFromUser")
    public ResponseEntity<Integer> addOrderFromUser (@RequestParam String username,
                                                     @RequestParam String password,
                                                     @RequestParam String bookidstr,
                                                     @RequestParam String bookcountstr,
                                                     @RequestParam String bookpricestr,
                                                     @RequestParam String receivername,
                                                     @RequestParam String address) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("bookidstr", bookidstr);
        jsonObject.put("bookcountstr", bookcountstr);
        jsonObject.put("bookpricestr", bookpricestr);
        jsonObject.put("receivername", receivername);
        jsonObject.put("address", address);

        orderProducer.sendMsg(jsonObject);
        return new ResponseEntity<>(OrderConstant.ORDER_INPROCESSING, HttpStatus.OK);
    }
    @GetMapping("/order/getOrders")
    public ResponseEntity<List<Order>> getOrders (String username, String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrders("ADMIN".equals(user.getRole()) ? 0 : user.getId()), HttpStatus.OK);
    }

    @GetMapping("/order/getOrdersByBook")
    public ResponseEntity<List<Order>> getOrdersByBook(String username, String password, String searchbookstr) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrdersByBook("ADMIN".equals(user.getRole()) ? 0 : user.getId(), searchbookstr), HttpStatus.OK);
    }

    @GetMapping("/order/getOrdersByDaterange")
    public ResponseEntity<List<Order>> getOrdersByDaterange(String username, String password,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd") Date startdate,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd") Date enddate) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrdersByDaterange("ADMIN".equals(user.getRole()) ? 0 : user.getId(), startdate, enddate), HttpStatus.OK);
    }

    @GetMapping("/order/getBookSales")
    public ResponseEntity<String> getBookSales(@RequestParam(required = false)
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") Date startdate,
                                               @RequestParam(required = false)
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") Date enddate) {
        return new ResponseEntity<>(orderService.getBooksSales(startdate, enddate), HttpStatus.OK);
    }
}
