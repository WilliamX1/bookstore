package com.bookstore.bookstore.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("bookimage")
public class BookImage {

    @Field("imageBase64")
    private String imageBase64;

    @Id
    private int bookId;

    public BookImage() {
    }

    public BookImage(String imageBase64, int bookId) {
        this.imageBase64 = imageBase64;
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
