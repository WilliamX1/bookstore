package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;

import java.util.Date;
import java.util.List;

public interface UserService {
    /*获取用户信息*/
    List<User> getUsers();
    /* 获取用户信息 */
    List<User> getUsers(Integer userId);
    /* 通过密码和用户名获取用户信息 */
    User getUserByUsernameAndPassword(String username, String password);
    /* 仅通过用户名获取用户信息 */
    User getUserByUsername(String username);
    /*修改用户*/
    Integer editUserState(Integer userid, String changedstate);
    /*用户注册*/
    User register(String username, String password, String email);
    /* 修改书购物车数量 */
    Integer changeBookCount (Integer userid, Integer bookid, Integer bookcount, Boolean isadd);
    /* 获取购物车 */
    List<CartItem> getCartItems (Integer userid);
    /* 通过查询书籍名获得购物车项 */
    List<CartItem> getCartItemsByBookname(Integer userid, String searchbookstr);
    /* 统计指定时间范围内各种书籍销量, 以JSON数据格式返回 */
    String getUserconsumptions(Date startend, Date enddate);
}
