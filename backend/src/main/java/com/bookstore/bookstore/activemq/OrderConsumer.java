package com.bookstore.bookstore.activemq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.constant.OrderConstant;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.OrderService;
import com.bookstore.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import java.util.List;

@Component
@Slf4j
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
                        try {
                            execute(order);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JMSException e) {
                        LOG.error("" + e);
                    }
                }
            });
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }

    private ResponseEntity<Integer> execute(JSONObject order) throws Exception {
        try {
            return new ResponseEntity<>(orderService.makeOrder(order), HttpStatus.OK);
        } catch (Exception e) {
            log.error("" + e);
            return new ResponseEntity<>(-1, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
