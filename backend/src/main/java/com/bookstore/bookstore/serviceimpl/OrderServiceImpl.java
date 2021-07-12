package com.bookstore.bookstore.serviceimpl;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;
import com.bookstore.bookstore.repo.OrderRepo;
import com.bookstore.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepo orderRepo;
    /* 添加订单信息 */
    public Integer addOrderFromUser(Integer userid, Integer price, String receivername, String address,
                                    List<Integer> bookid, List<Integer> bookcount, List<Integer> bookprice) {
        return orderRepo.addOrderFromUser(userid, price, receivername, address, bookid, bookcount, bookprice);
    };
    /* 获取订单信息 */
    public List<Order> getOrders (Integer userid) {
        return orderRepo.getOrders(userid);
    };
    /* 获取订单详情物品信息 */
    public List<OrderItem> getOrderItems (Integer userid) {
        return orderRepo.getOrderItems(userid);
    };
    /* 根据书名查找订单信息 */
    public List<Order> getOrdersByBook(Integer userid, String searchbookstr) {
        return orderRepo.getOrdersByBook(userid, searchbookstr);
    };
    /* 根据时间范围搜索订单信息 */
    public List<Order> getOrdersByDaterange(Integer userid, Date startdate, Date enddate) {
        return orderRepo.getOrdersByDaterange(userid, startdate, enddate);
    };
    /* 统计指定时间范围内各种书籍销量, 以JSON数据格式返回 */
    public String getBooksSales(Date startend, Date enddate) {
        return orderRepo.getBooksSales(startend, enddate);
    };
}
