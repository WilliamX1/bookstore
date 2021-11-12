package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.TagName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagNameRepository extends JpaRepository<TagName, Integer> {
    TagName findByName(String name);
}
