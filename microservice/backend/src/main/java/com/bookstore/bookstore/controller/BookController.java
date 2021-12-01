package com.bookstore.bookstore.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.BookImage;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/book/testBook")
    public String test () {
        return "This is a test book!";
    }

    @GetMapping("/book/getBooks")
    public ResponseEntity<List<Book>> getBooks (@RequestParam String username,
                                                @RequestParam String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }
    @GetMapping("/book/getBookImages")
    public ResponseEntity<List<BookImage>> getBookImages(@RequestParam String username,
                                                         @RequestParam String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(bookService.getBookImages(), HttpStatus.OK);
    }
    @GetMapping("/book/changeBookInventory")
    public ResponseEntity<Integer> changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) {
        try {
            return new ResponseEntity<>(bookService.changeBookInventory(bookid, changeinventory, isadd), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(-1, HttpStatus.NOT_ACCEPTABLE);
    }
    @GetMapping("/book/searchBookByBookname")
    public ResponseEntity<List<Book>> searchBookByBookname(String searchbookstr) {
        return new ResponseEntity<>(bookService.getBooksByBookname(searchbookstr), HttpStatus.OK);
    }
    @PostMapping("/book/editBookInfo")
    public ResponseEntity<Integer> editBookInfo(String bookstr, String imageBase64) {
        Book book = JSON.parseObject(bookstr, Book.class);
        return new ResponseEntity<>(bookService.editBookInfo(book, imageBase64), HttpStatus.OK);
    }
    @PostMapping("/book/deleteBook")
    public ResponseEntity<Integer> deleteBook(Integer id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
    @GetMapping("/book/fulltextSearchBook")
    public ResponseEntity<List<Book>> fulltextSearchBook(String searchbookstr) {
        return new ResponseEntity<>(bookService.fulltextSearchBook(searchbookstr), HttpStatus.OK);
    }
    @GetMapping("/book/getBooksByTag")
    public ResponseEntity<List<Book>> getBooksByTag(String searchbookstr) {
        return new ResponseEntity<>(bookService.getBooksByTag(searchbookstr), HttpStatus.OK);
    }
}
