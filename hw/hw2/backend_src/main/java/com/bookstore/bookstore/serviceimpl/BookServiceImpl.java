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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /*获取用户信息*/
    public List<Book> getBooks() {
        return bookRepository.getBooks();
    }

    /* 修改书籍库存 */
    @Transactional(propagation = Propagation.REQUIRED)
//    @Transactional(propagation = Propagation.MANDATORY)
    public Integer changeBookInventory(Integer bookid, Integer changeinventory, Boolean isadd) throws Exception {
        try {
            return bookRepository.changeBookInventory(bookid, changeinventory, isadd);
        } catch (Exception e) {
            throw new RuntimeException("图书数量不够");
        }
    }

    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookRepository.getBooksByBookname(searchbookstr);
    }

    ;

    /*修改图书信息*/
    public Integer editBookInfo(Book book) {
        return bookRepository.editBookInfo(book);
    }

    ;

    /*删除图书*/
    public Integer deleteBook(Integer bookid) {
        return bookRepository.deleteBook(bookid);
    }

    ;

    /* 处理消息队列中下订单逻辑 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer makeOrder(JSONObject order) {
        try {
            String username = "", password = "", bookidstr = "", bookcountstr = "",
                    bookpricestr = "", receivername = "", address = "";
            /* 获取 JSON 中变量值 */
            try {
                username = order.getString("username");
                Assert.notNull(username, "username is null");
                password = order.getString("password");
                Assert.notNull(password, "password is null");
                bookidstr = order.getString("bookidstr");
                Assert.notNull(bookidstr, "bookidstr is null");
                bookcountstr = order.getString("bookcountstr");
                Assert.notNull(bookcountstr, "bookcountstr is null");
                bookpricestr = order.getString("bookpricestr");
                Assert.notNull(bookpricestr, "bookpricestr is null");
                receivername = order.getString("receivername");
                Assert.notNull(receivername, "receivername is null");
                address = order.getString("address");
                Assert.notNull(address, "address is null");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("" + e);
            }

            /* 查找用户 */
            User user = null;
            List<Integer> bookid = null, bookcount = null, bookprice = null;
            try {
                user = userService.getUserByUsernameAndPassword(username, password);
                Assert.notNull(user, "user is not found");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("" + e);
            }
            /* 转换 JSON 数组 */
            try {
                bookid = JSON.parseArray(bookidstr, Integer.class);
                bookcount = JSON.parseArray(bookcountstr, Integer.class);
                bookprice = JSON.parseArray(bookpricestr, Integer.class);
            } catch (JSONException e) {
                throw new RuntimeException("" + e);
            }
            Integer userid = user.getId();
            /* 计算订单总价 */
            int len = bookid.size(), totalprice = 0;
            for (int i = 0; i < len; i++) {
                totalprice += bookcount.get(i) * bookprice.get(i);
            }

            for (int i = 0; i < len; i++) {
                try {
                    changeBookInventory(bookid.get(i), -bookcount.get(i), true);
                    userService.changeBookCount(userid, bookid.get(i), -bookcount.get(i), true);
                } catch (Exception e) {
                    throw new RuntimeException("" + e);
                }
            }
            return orderService.addOrderFromUser(userid, totalprice, receivername, address, bookid, bookcount, bookprice);
        } catch (Exception e) {
            log.error("" + e);
            return -1;
        }
    }

    ;
}
