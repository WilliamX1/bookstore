package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    User findById(Integer id);
}
