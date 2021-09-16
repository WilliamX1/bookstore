package com.bookstore.bookstore.repositoryimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private BookDao bookDao;

    /*获取图书信息*/
    public List<Book> getBooks () {
        return bookDao.findByStateEquals(1);
    }
    /* 修改书籍库存 */
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) {
        Book book = bookDao.findById(bookid);
        if (isadd) book.setInventory(book.getInventory() + changeinventory);
        else book.setInventory(changeinventory);
        bookDao.save(book);
        return book.getInventory() >= 0 ? 0 : -1;
    }
    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookDao.findByBooknameContaining(searchbookstr);
    };
    /*修改图书信息*/
    public Integer editBookInfo(Book book) {
        bookDao.save(book);
        return 0;
    };
    /*删除图书*/
    public Integer deleteBook(Integer bookid) {
        Book book = bookDao.findById(bookid);
        book.setState(0);
        bookDao.save(book);
        return 0;
    };
}
