package com.bookstore.bookstore.serviceimpl;

import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    /*获取用户信息*/
    public List<User> getUsers () {
        return userRepository.getUsers();
    }
    /* 获取用户 */
    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }
    /* 仅通过用户名获取用户信息 */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    };
    /* 修改用户 */
    public Integer editUserState(Integer userid, String changedstate) { return userRepository.editUserState(userid, changedstate); };
    /* 用户注册 */
    public User register(String username, String password, String email) { return userRepository.register(username, password, email); };
    /* 修改书籍数量 */
    public Integer changeBookCount (Integer userid, Integer bookid, Integer bookcount, Boolean isadd) {
        return userRepository.changeBookCount(userid, bookid, bookcount, isadd);
    }
    /* 获取购物车 */
    public List<CartItem> getCartItems (Integer userid) {
        return userRepository.getCartItems(userid);
    };
    /* 通过查询书籍名获得购物车项 */
    public List<CartItem> getCartItemsByBookname(Integer userid, String searchbookstr) {
        return userRepository.getCartItemsByBookname(userid, searchbookstr);
    };
    /* 获取某一阶段用户消费情况 */
    public String getUserconsumptions(Date startdate, Date enddate) {
        return userRepository.getUserconsumptions(startdate, enddate);
    };
}
