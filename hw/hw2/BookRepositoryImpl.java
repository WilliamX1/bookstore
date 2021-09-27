package com.bookstore.bookstore.repositoryimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) throws Exception {
        Book book = null;
        try {
            book = bookDao.findById(bookid);
            Assert.notNull(book);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("" + e);
        }
        if (isadd) book.setInventory(book.getInventory() + changeinventory);
        else book.setInventory(changeinventory);
        try {
            Assert.state(book.getInventory() >= 0, "图书数量不够");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("图书数量不够");
        };
        bookDao.save(book);
        return 0;
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
