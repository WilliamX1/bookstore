package com.bookstore.bookstore.activemq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.constant.OrderConstant;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import java.util.List;

@Component
public class OrderConsumer implements CommandLineRunner {

    private static QueueConsumer queueConsumer;

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Override
    public void run(String... args) {
        queueConsumer = new QueueConsumer(OrderConstant.QUEUE_NAME);
        Logger LOG = queueConsumer.getLog();
        MessageConsumer messageConsumer = queueConsumer.getConsumer();

        receiveAndExecuteMsg(LOG, messageConsumer);
    }

    private void receiveAndExecuteMsg(Logger LOG, MessageConsumer messageConsumer) {
        try {
            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        ActiveMQObjectMessage activeMQObjectMessage = (ActiveMQObjectMessage) message;
                        JSONObject order = (JSONObject) activeMQObjectMessage.getObject();
                        LOG.info(Thread.currentThread().getName() + " receive a message : {}", activeMQObjectMessage.getObject());
                        execute(order);
                    } catch (JMSException e) {
                        LOG.error("" + e);
                    }
                }
            });
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }

    private ResponseEntity<Integer> execute(JSONObject order) {
        String username = order.getString("username");
        String password = order.getString("password");
        String bookidstr = order.getString("bookidstr");
        String bookcountstr = order.getString("bookcountstr");
        String bookpricestr = order.getString("bookpricestr");
        String receivername = order.getString("receivername");
        String address = order.getString("address");

        User user = userService.getUserByUsernameAndPassword(username, password);
        List<Integer> bookid = JSON.parseArray(bookidstr, Integer.class);
        List<Integer> bookcount = JSON.parseArray(bookcountstr, Integer.class);
        List<Integer> bookprice = JSON.parseArray(bookpricestr, Integer.class);

        if (user == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else {
            Integer userid = user.getId();
            /*计算订单总价*/
            int len = bookid.size();
            int totalprice = 0;
            for (Integer i = 0; i < len; i++) {
                totalprice += bookcount.get(i) * bookprice.get(i);
            }

            for (Integer i = 0; i < len; i++) {
                if (bookService.changeBookInventory(bookid.get(i), -bookcount.get(i), true) == -1) {
                    /* 将其他书籍库存加回来 */
                    for (Integer j = i; j >= 0; j--) {
                        bookService.changeBookInventory(bookid.get(j), bookcount.get(j), true);
                    };
                    return new ResponseEntity<>(-1, HttpStatus.NOT_ACCEPTABLE);
                };
            };
            for (Integer i = 0; i < len; i++) {
                userService.changeBookCount(userid, bookid.get(i), -bookcount.get(i), true);
            }

            Integer ans = orderService.addOrderFromUser(userid, totalprice, receivername, address, bookid, bookcount, bookprice);
            return new ResponseEntity<>(ans, HttpStatus.OK);
        }
    }
}
