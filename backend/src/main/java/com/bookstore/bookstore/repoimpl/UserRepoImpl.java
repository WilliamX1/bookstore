package com.bookstore.bookstore.repoimpl;

import com.alibaba.fastjson.JSON;
import com.bookstore.bookstore.entity.*;
import com.bookstore.bookstore.repo.UserRepo;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.repository.CartRepository;
import com.bookstore.bookstore.repository.OrderRepository;
import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepoImpl implements UserRepo {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    /* 获取用户 */
    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    };
    /* 仅通过用户名获取用户信息 */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    };
    /*获取用户信息*/
    public List<User> getUsers () {
        return userRepository.findAll();
    }
    /*修改用户*/
    public Integer editUserState(Integer userId, String changedState) {
        User user = userRepository.findById(userId);
        user.setState(changedState);
        userRepository.save(user);
        return 0;
    }
    /*用户注册*/
    public User register(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userRepository.save(user);
        return user;
    }
    /* 修改购物车书籍数量 */
    public Integer changeBookCount (Integer userid, Integer bookid, Integer bookcount, Boolean isadd) {
        /*保存购物车信息*/
        User user = userRepository.findById(userid);
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            userRepository.save(user);
        }

        /*保存购物车内容信息*/
        List<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = null;

        /* 寻找购物车项 */
        Book book = bookRepository.findById(bookid);
        for (CartItem item : cartItems) {
            if (book.equals(item.getBook())) {
                cartItem = item;
                break;
            }
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setBookcount(0);
            cartItem.setCart(cart);
        }
        /*是否是添加/设置*/
        if (isadd) cartItem.setBookcount(cartItem.getBookcount() + bookcount);
        else cartItem.setBookcount(bookcount);

        cart.addCartItem(cartItem);
        /*将数量为0的书籍从购物车里删除*/
        if (cartItem.getBookcount() == 0)
            cart.deleteCartItem(cartItem);

        cartRepository.save(cart);
        return 0;
    }
    /* 获取购物车 */
    public List<CartItem> getCartItems (Integer userid) {
        User user = userRepository.findById(userid);
        Cart cart = user.getCart();
        if (cart != null) return cart.getCartItems();
        else return new ArrayList<>();
    };
    /* 通过查询书籍名获得购物车项 */
    public List<CartItem> getCartItemsByBookname(Integer userid, String searchbookstr) {
        List<CartItem> cartItems = getCartItems(userid);

        List<Book> books = bookRepository.findByBooknameContaining(searchbookstr);
        List<Integer> bookids = new ArrayList<>();
        books.forEach(book -> {
            bookids.add(book.getId());
        });
        System.out.println(bookids);
        return cartItems.stream().filter(cartItem -> bookids.contains(cartItem.getBook().getId())).collect(Collectors.toList());
    };
    /* 获取某一阶段用户消费情况 */
    public String getUserconsumptions(Date startdate, Date enddate) {
        /* 获取指定时间内订单项 */
        List<Order> orders;
        if (startdate == null && enddate == null) {
            orders = orderRepository.findAll();
        } else orders = orderRepository.findAllByTimestampBetween(startdate, enddate);

        /* 对用户消费进行统计 */
        Map<String, Integer> map = new HashMap<>();
        orders.forEach(order -> {
            String userid = order.getUser().getId().toString();
            Integer orderid = order.getId();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> {
                if (map.containsKey(userid)) map.put(userid, map.get(userid).intValue() + order.getPrice());
                else map.put(userid, order.getPrice());
            });
        });
        return JSON.toJSONString(map);
    };
}
