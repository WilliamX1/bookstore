package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;

import java.util.Date;
import java.util.List;

public interface UserRepository {
    /* 获取用户 */
    User getUserByUsernameAndPassword(String username, String password);
    /* 仅通过用户名获取用户信息 */
    User getUserByUsername(String username);
    /*获取用户信息*/
    List<User> getUsers ();
    /* 获取用户信息 */
    List<User> getUsers(Integer userId);
    /*修改用户*/
    Integer editUserState(Integer userid, String changedstate);
    /*用户注册*/
    User register(String username, String password, String email);
    /* 修改购物车书籍数量 */
    Integer changeBookCount (Integer userid, Integer bookid, Integer bookcount, Boolean isadd) throws Exception;
    /* 获取购物车 */
    List<CartItem> getCartItems (Integer userid);
    /* 通过查询书籍名获得购物车项 */
    List<CartItem> getCartItemsByBookname(Integer userid, String searchbookstr);
    /* 获取某一阶段用户消费情况 */
    String getUserconsumptions(Date startdate, Date enddate);
}
