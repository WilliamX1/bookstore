package com.bookstore.bookstore.controller;

import com.alibaba.fastjson.JSON;
import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
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

    @GetMapping("/testOrder")
    public String test () { return "This is a test order!"; }

    @PostMapping("/addOrderFromUser")
    public ResponseEntity<Integer> addOrderFromUser (@RequestParam String username,
                                                     @RequestParam String password,
                                                     @RequestParam String bookidstr,
                                                     @RequestParam String bookcountstr,
                                                     @RequestParam String bookpricestr,
                                                     @RequestParam String receivername,
                                                     @RequestParam String address) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        List<Integer> bookid = JSON.parseArray(bookidstr, Integer.class);
        List<Integer> bookcount = JSON.parseArray(bookcountstr, Integer.class);
        List<Integer> bookprice = JSON.parseArray(bookpricestr, Integer.class);

        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else {
            Integer userid = user.getId();
            /*计算订单总价*/
            Integer len = bookid.size();
            Integer totalprice = 0;
            for (Integer i = 0; i < len; i++) {
                totalprice += bookcount.get(i) * bookprice.get(i);
            }

            for (Integer i = 0; i < len; i++) {
                if (bookService.changeBookInventory(bookid.get(i), -bookcount.get(i), true) == -1) {
                    /* 将其他书籍库存加回来 */
                    for (Integer j = i; j >= 0; j--) {
                        bookService.changeBookInventory(bookid.get(j), bookcount.get(j), true);
                    };
                    return new ResponseEntity<>(-1, HttpStatus.NOT_ACCEPTABLE);
                };
            };
            for (Integer i = 0; i < len; i++) {
                userService.changeBookCount(userid, bookid.get(i), -bookcount.get(i), true);
            }

            Integer ans = orderService.addOrderFromUser(userid, totalprice, receivername, address, bookid, bookcount, bookprice);
            return new ResponseEntity<>(ans, HttpStatus.OK);
        }
    }
    @GetMapping("getOrders")
    public ResponseEntity<List<Order>> getOrders (String username, String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrders("ADMIN".equals(user.getRole()) ? 0 : user.getId()), HttpStatus.OK);
    }

    @GetMapping("getOrdersByBook")
    public ResponseEntity<List<Order>> getOrdersByBook(String username, String password, String searchbookstr) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrdersByBook("ADMIN".equals(user.getRole()) ? 0 : user.getId(), searchbookstr), HttpStatus.OK);
    }

    @GetMapping("getOrdersByDaterange")
    public ResponseEntity<List<Order>> getOrdersByDaterange(String username, String password,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd") Date startdate,
                                                            @DateTimeFormat(pattern="yyyy-MM-dd") Date enddate) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(orderService.getOrdersByDaterange("ADMIN".equals(user.getRole()) ? 0 : user.getId(), startdate, enddate), HttpStatus.OK);
    }

    @GetMapping("getBookSales")
    public ResponseEntity<String> getBookSales(@RequestParam(required = false)
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") Date startdate,
                                               @RequestParam(required = false)
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") Date enddate) {
        return new ResponseEntity<>(orderService.getBooksSales(startdate, enddate), HttpStatus.OK);
    }
}
