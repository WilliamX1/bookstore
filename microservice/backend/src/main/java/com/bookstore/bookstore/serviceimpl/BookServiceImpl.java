package com.bookstore.bookstore.serviceimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.dao.BookDao;
import com.bookstore.bookstore.entity.BookImage;
import com.bookstore.bookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "book")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    /* 获取用户信息 */
    public List<Book> getBooks () {
        return bookDao.getBooks();
    }
    /* 修改书籍库存 */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer changeBookInventory (Integer bookid, Integer changeinventory, Boolean isadd) throws Exception {
        try {
            return bookDao.changeBookInventory(bookid, changeinventory, isadd);
        } catch (Exception e) {
            throw new RuntimeException("图书数量不够");
        }
    }
    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        return bookDao.getBooksByBookname(searchbookstr);
    };
    /* 修改图书信息 */
    public Integer editBookInfo(Book book, String bookImageBase64) {
        return bookDao.editBookInfo(book, bookImageBase64);
    };
    /* 删除图书 */
    public Integer deleteBook(Integer bookid) {
        return bookDao.deleteBook(bookid);
    }
    /* 全文搜索书籍 */
    public List<Book> fulltextSearchBook(String text) {
        return bookDao.fulltextSearchBook(text);
    };

    /* 获取图片照片信息 */
    public List<BookImage> getBookImages() {
        return bookDao.getBookImages();
    }

    /* 通过 tag 模糊搜索获取书籍 */
    public List<Book> getBooksByTag(String tag) {
        return bookDao.getBooksByTag(tag);
    };
}
