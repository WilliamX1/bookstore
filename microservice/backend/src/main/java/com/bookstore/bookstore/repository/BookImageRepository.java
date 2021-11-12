package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.BookImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookImageRepository extends MongoRepository<BookImage, Integer> {
    BookImage findByBookId(Integer bookId);
}
