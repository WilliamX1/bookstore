package com.bookstore.bookstore.dao;

import com.bookstore.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {
    Book findById(Integer id);
    Book findByIdAndStateEquals(Integer id, Integer state);
    List<Book> findAll();
    List<Book> findByBooknameContaining(String searchbookstr);
    List<Book> findByStateEquals(Integer state);
}
