package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Book;

import java.util.List;

public interface BookService {
    /*获取用户信息*/
    List<Book> getBooks ();
    /* 修改书籍库存 */
    Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd);
    /* 根据书名查找书籍 */
    List<Book> getBooksByBookname(String searchbookstr);
    /* 修改图书信息 */
    Integer editBookInfo(Book book);
    /* 删除图书 */
    Integer deleteBook(Integer bookid);
}
