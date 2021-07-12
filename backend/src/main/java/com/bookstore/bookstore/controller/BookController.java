package com.bookstore.bookstore.controller;

import com.alibaba.fastjson.JSON;
import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/testBook")
    public String test () {
        return "This is a test book!";
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> getBooks (@RequestParam String username,
                                                @RequestParam String password) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }
    @GetMapping("/changeBookInventory")
    public ResponseEntity<Integer> changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) {
        return new ResponseEntity<>(bookService.changeBookInventory(bookid, changeinventory, isadd), HttpStatus.OK);
    }
    @GetMapping("/searchBookByBookname")
    public ResponseEntity<List<Book>> searchBookByBookname(String searchbookstr) {
        return new ResponseEntity<>(bookService.getBooksByBookname(searchbookstr), HttpStatus.OK);
    }
    @PostMapping("/editBookInfo")
    public ResponseEntity<Integer> editBookInfo(String bookstr) {
        Book book = JSON.parseObject(bookstr, Book.class);
        System.out.println(book);
        return new ResponseEntity<>(bookService.editBookInfo(book), HttpStatus.OK);
    }
    @PostMapping("/deleteBook")
    public ResponseEntity<Integer> deleteBook(Integer id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }
}
