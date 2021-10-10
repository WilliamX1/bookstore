package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @GetMapping("/user/testUser")
    public String test () {
        return "This is a test user!";
    }

    @GetMapping("/user/apiVisitHistory")
    public void apiVisitHistory() {}

    @GetMapping("/user/verifyUsername")
    public ResponseEntity<Integer> verifyUsername(String username) {
        System.out.println(username);
        User user = userService.getUserByUsername(username);
        System.out.println(user);
        return new ResponseEntity<>(user == null ? 1 : 0, HttpStatus.OK);
    }
    @GetMapping("/user/checkGotoHome") /*检查是否能进入首页*/
    public ResponseEntity<User> checkUserRole (@RequestParam (required = false) String username,
                                                 @RequestParam (required = false) String password) {
        UserService userService = webApplicationContext.getBean(UserService.class);
        System.out.println(userService);
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else if ("Normal".equals(user.getState())) return new ResponseEntity<>(user, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/user/getUsers")
    public ResponseEntity<List<User>> getUsers (@RequestParam (required = false) String username,
                                                 @RequestParam (required = false) String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        System.out.println(userService);
        if (user != null && "ADMIN".equals(user.getRole())) return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
    @GetMapping("/user/getUsers/{userId}")
    public ResponseEntity<List<User>> getUsers(@PathVariable("userId") Integer userId) {
        UserService userService = webApplicationContext.getBean(UserService.class);
        System.out.println(userService);
        return new ResponseEntity<>(userService.getUsers(userId), HttpStatus.OK);
    }
    @PostMapping("/user/editUser")
    public ResponseEntity<Integer> editUserState(String username, String password, Integer userid, String changedstate) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user != null && "ADMIN".equals((user.getRole()))) return new ResponseEntity<>(userService.editUserState(userid, changedstate), HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/user/register")
    public ResponseEntity<User> register(String username, String password, String email) {
        if (userService.getUserByUsername(username) != null) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        else return new ResponseEntity<>(userService.register(username, password, email), HttpStatus.OK);
    }

    @PostMapping("/user/changeBookCountTo")
    public ResponseEntity<Integer> changeBookCountTo(String username, String password, Integer bookid, Integer bookcount) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        else {
            try {
                return new ResponseEntity<>(userService.changeBookCount(user.getId(), bookid, bookcount, false), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }
    @PostMapping("/user/changeBookCountAdd")
    public ResponseEntity<Integer> changeBookCountAdd(String username, String password, Integer bookid, Integer bookcount) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        else {
            try {
                return new ResponseEntity<>(userService.changeBookCount(user.getId(), bookid, bookcount, true), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @GetMapping("/user/getCartItems")
    public ResponseEntity<List<CartItem>> getCartItems(String username, String password, String searchbookstr) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        System.out.println(searchbookstr);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else if ("".equals(searchbookstr)) return new ResponseEntity<>(userService.getCartItems(user.getId()), HttpStatus.OK);
        else return new ResponseEntity<>((userService.getCartItemsByBookname(user.getId(), searchbookstr)), HttpStatus.OK);
    }

    @GetMapping("/user/getUserconsumptions")
    public ResponseEntity<String> getUserconsumptions(@RequestParam(required = false)
                                               @DateTimeFormat(pattern="yyyy-MM-dd") Date startdate,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern="yyyy-MM-dd") Date enddate) {
        return new ResponseEntity<>(userService.getUserconsumptions(startdate, enddate), HttpStatus.OK);
    }

}
