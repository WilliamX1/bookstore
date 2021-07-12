package com.bookstore.bookstore.serviceimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repo.BookRepo;
import com.bookstore.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepo bookRepo;
    /*获取用户信息*/
    public List<Book> getBooks () {
        return bookRepo.getBooks();
    }
    /* 修改书籍库存 */
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) {
        return bookRepo.changeBookInventory(bookid, changeinventory, isadd);
    }
    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookRepo.getBooksByBookname(searchbookstr);
    };
    /*修改图书信息*/
    public Integer editBookInfo(Book book) {
        return bookRepo.editBookInfo(book);
    };
    /*删除图书*/
    public Integer deleteBook(Integer bookid) {
        return bookRepo.deleteBook(bookid);
    };
}
