package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findById(Integer id);
    List<Order> findAllByTimestampBetween(Date startdate, Date enddate);
    List<Order> findAll();
}
