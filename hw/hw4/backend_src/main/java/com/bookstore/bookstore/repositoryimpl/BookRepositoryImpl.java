package com.bookstore.bookstore.repositoryimpl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.fulltextsearching.ReadWriteFiles;
import com.bookstore.bookstore.fulltextsearching.FilesPositionConfig;
import com.bookstore.bookstore.fulltextsearching.SearchFiles;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.dao.BookDao;
import com.bookstore.bookstore.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private BookDao bookDao;

    @Autowired
    RedisUtil redisUtil;


    /* 获取图书信息 */
    public List<Book> getBooks() {
        List<Book> result = new ArrayList<>();
        for (int i = 1; ; i++) {
            Book book = getBookById(i);
            /* 已经查阅完全部书籍则返回 */
            if (book == null) break;
            /* 如果该书没有被删除 */
            if (book.getState() == 1) result.add(book);
        }
        return result;
    }

    /* 根据 book_id 获取某本书信息 */
    public Book getBookById(Integer bookid) {
        Book book;
        Object o = redisUtil.get("book-" + bookid);
        if (o == null) {
            book = bookDao.findById(bookid);
            /* 已经查阅完全部书籍则返回 */
            if (book == null) return null;
            else {
                System.out.println(book);
                redisUtil.set("book-" + bookid, book);
            }
        } else book = (Book) o;
        return book;
    }

    /* 修改书籍库存 */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Integer changeBookInventory(Integer bookid, Integer changeinventory, Boolean isadd) throws Exception {
        Book book = null;
        try {
            Object o = redisUtil.get("book-" + bookid);
            /* 如果不在缓存里 */
            if (o == null) book = bookDao.findById(bookid);
            else book = (Book) o;
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
        }
        ;
        bookDao.save(book);
        /* 更新缓存 */
        redisUtil.set("book-" + bookid, book);
        return 0;
    }

    /* 根据书名查找书籍 */
    public List<Book> getBooksByBookname(String searchbookstr) {
        /* 先将没有缓存的书籍缓存至 redis */
        getBooks();
        List<Book> result = new ArrayList<>();
        Book book;
        for (int i = 1; ; i++) {
            Object o = redisUtil.get("book-" + i);
            if (o == null) break; /* 查询完所有书籍 */
            else {
                book = (Book) o;
                if (book.getBookname().contains(searchbookstr)) result.add(book);
            }
        }
        ;
        return result;
//        return bookDao.findByBooknameContaining(searchbookstr);
    }

    ;

    /* 修改图书信息 */
    public Integer editBookInfo(Book book) {
        bookDao.save(book);
        /* 创建书籍信息 docs 并创建相应索引 */
        ReadWriteFiles.create_docs_files(book.getId(), book.getIntroduction(), true);
        /* 存入 redis 缓存中 */
        redisUtil.set("book-" + book.getId(), book);

        return 0;
    }

    ;

    /* 删除图书 */
    public Integer deleteBook(Integer bookid) {
        Book book = null;
        Object o = redisUtil.get("book-" + bookid);
        if (o == null) book = bookDao.findById(bookid);
        else book = (Book) o;

        Assert.notNull(book);

        book.setState(0);
        bookDao.save(book);
        /* 存入 redis 缓存中 */
        redisUtil.set("book-" + bookid, book);
        return 0;
    }

    ;

    /* 全文搜索书籍 */
    public List<Book> fulltextSearchBook(String text) {
        List<Book> bookList = new ArrayList<>();
        try {
            String[] args = {"-index", FilesPositionConfig.indexPath, "-query", text};
            List<Integer> bookidList = SearchFiles.search_interface(args);
            for (Integer integer : bookidList) {
                Book book = getBookById(integer);
                /* 如果该书没有被删除 */
                if (book != null && book.getState() == 1) bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    ;
}
