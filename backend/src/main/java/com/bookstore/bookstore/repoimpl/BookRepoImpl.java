package com.bookstore.bookstore.repoimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.repo.BookRepo;
import com.bookstore.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepoImpl implements BookRepo {
    @Autowired
    private BookRepository bookRepository;

    /*获取图书信息*/
    public List<Book> getBooks () {
        return bookRepository.findByStateEquals(1);
    }
    /* 修改书籍库存 */
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) {
        Book book = bookRepository.findById(bookid);
        if (isadd) book.setInventory(book.getInventory() + changeinventory);
        else book.setInventory(changeinventory);
        bookRepository.save(book);
        return book.getInventory() >= 0 ? 0 : -1;
    }
    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookRepository.findByBooknameContaining(searchbookstr);
    };
    /*修改图书信息*/
    public Integer editBookInfo(Book book) {
        bookRepository.save(book);
        return 0;
    };
    /*删除图书*/
    public Integer deleteBook(Integer bookid) {
        Book book = bookRepository.findById(bookid);
        book.setState(0);
        bookRepository.save(book);
        return 0;
    };
}
