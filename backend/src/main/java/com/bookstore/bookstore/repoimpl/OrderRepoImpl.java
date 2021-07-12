package com.bookstore.bookstore.repoimpl;

import com.alibaba.fastjson.JSON;
import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.repo.OrderRepo;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.repository.OrderItemRepository;
import com.bookstore.bookstore.repository.OrderRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderRepoImpl implements OrderRepo {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    /* 添加订单信息 */
    public Integer addOrderFromUser(Integer userid, Integer price, String receivername, String address,
                                    List<Integer> bookid, List<Integer> bookcount, List<Integer> bookprice) {
        System.out.println("*(*&*&^*&(*&&^&&*&*^&*(')");
        Order order = new Order();

        Integer len = bookid.size();
        for (Integer i = 0; i < len; i++) {
            OrderItem orderItem = new OrderItem();
            Book book = bookRepository.findById(bookid.get(i));
            orderItem.setBook(book);
            orderItem.setBookcount(bookcount.get(i));
            orderItem.setBookprice(bookprice.get(i));
            orderItem.setOrder(order);
            order.addOrderItem(orderItem);
        };
        User user = userRepository.findById(userid);
        order.setUser(user);
        order.setPrice(price);
        order.setReceivername(receivername);
        order.setAddress(address);
        user.addOrderList(order);
        userRepository.save(user);
        return order.getId();
    };
    /* 获取订单信息 */
    public List<Order> getOrders (Integer userid) {
        /* 管理员获取全部 */
        if (userid == 0) {
            List<Order> orders = new ArrayList<>();
            List<User> users = userRepository.findAll();
            users.forEach(user -> {
                orders.addAll(user.getOrderList());
            });
            return orders;
        }
        else return userRepository.findById(userid).getOrderList();
    };
    /* 获取订单详情物品信息 */
    public List<OrderItem> getOrderItems (Integer userid) {
        if (userid == 0) {
            List<User> users = userRepository.findAll();
            List<OrderItem> orderItems = new ArrayList<>();
            users.forEach(user -> {
                orderItems.addAll(getOrderItems(user.getId()));
            });
            return orderItems;
        }
        else {
            List<Order> orders = getOrders(userid);
            List<OrderItem> orderItems = new ArrayList<>();
            orders.forEach(order -> {
                orderItems.addAll(order.getOrderItems());
            });
            return orderItems;
        }
    };
    /* 根据书名查找订单信息 */
    public List<Order> getOrdersByBook(Integer userid, String searchbookstr) {
        List<Book> books = bookRepository.findByBooknameContaining(searchbookstr);
        List<OrderItem> orderItems = getOrderItems(userid);

        Set<Integer> orderids = new HashSet<>();
        books.forEach(book -> {
            orderItems.forEach(orderItem -> {
                if (book.getId().equals(orderItem.getBook().getId())) {
                    orderids.add(orderItem.getOrderId());
                }
            });
        });

        List<Order> orders = new ArrayList<>();
        orderids.forEach(orderid -> {
            orders.addAll(orderRepository.findById(orderid));
        });

        return orders;
    };

    /* 根据时间范围搜索订单信息 */
    public List<Order> getOrdersByDaterange(Integer userid, Date startdate, Date enddate) {
        List<Order> historyOrdersAccordingTime = orderRepository.findAllByTimestampBetween(startdate, enddate);
        List<Order> historyOrdersAccordingUserid = getOrders(userid);

        System.out.println(userid);

        return historyOrdersAccordingTime.stream().
                filter((order) -> historyOrdersAccordingUserid.contains(order)).
                collect(Collectors.toList());
    };

    /* 统计指定时间范围内各种书籍销量, 以JSON数据格式返回 */
    public String getBooksSales(Date startdate, Date enddate) {
        /* 获取指定时间范围内订单项 */
        List<Order> orders;
        if (startdate == null && enddate == null) {
            orders = orderRepository.findAll();
        } else orders = orderRepository.findAllByTimestampBetween(startdate, enddate);

        /* 筛选相应订单 */
        List<OrderItem> orderItems = new ArrayList<>();
        orders.forEach(order -> {
            orderItems.addAll(order.getOrderItems());
        });

        /* 对Book和销量进行统计 */
        Map<String, Integer> map = new HashMap<>();
        orderItems.forEach(orderItem -> {
            String bookid = orderItem.getBook().getId().toString();
            Integer bookcount = orderItem.getBookcount();
            if (map.containsKey(bookid)) map.put(bookid, map.get(bookid).intValue() + bookcount);
            else map.put(bookid, bookcount);
        });
        return JSON.toJSONString(map);
    };
}
