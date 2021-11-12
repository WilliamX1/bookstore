package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Tag;
import com.bookstore.bookstore.entity.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findByTagName(TagName tagName);
}
