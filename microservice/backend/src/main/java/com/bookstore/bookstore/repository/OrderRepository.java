package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;

import java.util.Date;
import java.util.List;

public interface OrderRepository {
    /* 添加订单信息 */
    Integer addOrderFromUser(Integer userid, Integer price, String receivername, String address,
                             List<Integer> bookid, List<Integer> bookcount, List<Integer> bookprice);
    /* 获取订单信息 */
    List<Order> getOrders (Integer userid);
    /* 获取订单详情物品信息 */
    List<OrderItem> getOrderItems (Integer userid);
    /* 根据书名查找订单信息 */
    List<Order> getOrdersByBook(Integer userid, String searchbookstr);
    /* 根据时间范围搜索订单信息 */
    List<Order> getOrdersByDaterange(Integer userid, Date startdate, Date enddate);
    /* 统计指定时间范围内各种书籍销量, 以JSON数据格式返回 */
    String getBooksSales(Date startend, Date enddate);
}
