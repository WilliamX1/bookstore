package com.bookstore.bookstore.service;

import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.entity.Book;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookService {
    /*获取用户信息*/
    List<Book> getBooks ();
    /* 修改书籍库存 */
    @Transactional(propagation = Propagation.MANDATORY)
    Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) throws Exception;
    /* 根据书名查找书籍 */
    List<Book> getBooksByBookname(String searchbookstr);
    /* 修改图书信息 */
    Integer editBookInfo(Book book);
    /* 删除图书 */
    Integer deleteBook(Integer bookid);
    /* 处理消息队列中下订单逻辑 */
    Integer makeOrder(JSONObject order);
}