package com.bookstore.bookstore.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "book")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    /* 获取用户信息 */
    public List<Book> getBooks () {
        return bookRepository.getBooks();
    }
    /* 修改书籍库存 */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) throws Exception {
        try {
            return bookRepository.changeBookInventory(bookid, changeinventory, isadd);
        } catch (Exception e) {
            throw new RuntimeException("图书数量不够");
        }
    }
    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookRepository.getBooksByBookname(searchbookstr);
    };
    /* 修改图书信息 */
    public Integer editBookInfo(Book book) {
        return bookRepository.editBookInfo(book);
    };
    /* 删除图书 */
    public Integer deleteBook(Integer bookid) {
        return bookRepository.deleteBook(bookid);
    }
}
