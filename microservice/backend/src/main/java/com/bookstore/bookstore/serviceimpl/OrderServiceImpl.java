package com.bookstore.bookstore.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.dao.OrderDao;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private OrderService orderService;
    /* 添加订单信息 */
    public Integer addOrderFromUser(Integer userid, Integer price, String receivername, String address,
                                    List<Integer> bookid, List<Integer> bookcount, List<Integer> bookprice) {
        return orderDao.addOrderFromUser(userid, price, receivername, address, bookid, bookcount, bookprice);
    };
    /* 获取订单信息 */
    public List<Order> getOrders (Integer userid) {
        return orderDao.getOrders(userid);
    };
    /* 获取订单详情物品信息 */
    public List<OrderItem> getOrderItems (Integer userid) {
        return orderDao.getOrderItems(userid);
    };
    /* 根据书名查找订单信息 */
    public List<Order> getOrdersByBook(Integer userid, String searchbookstr) {
        return orderDao.getOrdersByBook(userid, searchbookstr);
    };
    /* 根据时间范围搜索订单信息 */
    public List<Order> getOrdersByDaterange(Integer userid, Date startdate, Date enddate) {
        return orderDao.getOrdersByDaterange(userid, startdate, enddate);
    };
    /* 统计指定时间范围内各种书籍销量, 以JSON数据格式返回 */
    public String getBooksSales(Date startend, Date enddate) {
        return orderDao.getBooksSales(startend, enddate);
    };
    /* 处理消息队列中下订单逻辑 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer makeOrder(JSONObject order) {
        try {
            String username = "", password = "", bookidstr = "", bookcountstr = "",
                    bookpricestr = "", receivername = "", address = "";
            /* 获取 JSON 中变量值 */
            try {
                username = order.getString("username");
                Assert.notNull(username, "username is null");
                password = order.getString("password");
                Assert.notNull(password, "password is null");
                bookidstr = order.getString("bookidstr");
                Assert.notNull(bookidstr, "bookidstr is null");
                bookcountstr = order.getString("bookcountstr");
                Assert.notNull(bookcountstr, "bookcountstr is null");
                bookpricestr = order.getString("bookpricestr");
                Assert.notNull(bookpricestr, "bookpricestr is null");
                receivername = order.getString("receivername");
                Assert.notNull(receivername, "receivername is null");
                address = order.getString("address");
                Assert.notNull(address, "address is null");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("" + e);
            }

            /* 查找用户 */
            User user = null;
            List<Integer> bookid = null, bookcount = null, bookprice = null;
            try {
                user = userService.getUserByUsernameAndPassword(username, password);
                Assert.notNull(user, "user is not found");
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("" + e);
            }
            /* 转换 JSON 数组 */
            try {
                bookid = JSON.parseArray(bookidstr, Integer.class);
                bookcount = JSON.parseArray(bookcountstr, Integer.class);
                bookprice = JSON.parseArray(bookpricestr, Integer.class);
            } catch (JSONException e) {
                throw new RuntimeException("" + e);
            }
            Integer userid = user.getId();
            /* 计算订单总价 */
            int len = bookid.size(), totalprice = 0;
            for (int i = 0; i < len; i++) {
                totalprice += bookcount.get(i) * bookprice.get(i);
            }

            for (int i = 0; i < len; i++) {
                try {
                    bookService.changeBookInventory(bookid.get(i), -bookcount.get(i), true);
                    userService.changeBookCount(userid, bookid.get(i), -bookcount.get(i), true);
                } catch (Exception e) {
                    throw new RuntimeException("" + e);
                }
            }
            return orderService.addOrderFromUser(userid, totalprice, receivername, address, bookid, bookcount, bookprice);
        } catch (Exception e) {
            log.error("" + e);
            return -1;
        }
    };
}
