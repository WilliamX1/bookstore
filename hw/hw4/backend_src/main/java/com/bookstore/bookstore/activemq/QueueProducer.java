package com.bookstore.bookstore.activemq;

import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.config.ActiveMqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

public class QueueProducer {
    private final Logger LOG = LoggerFactory.getLogger(QueueProducer.class);
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public QueueProducer(String queueName) {
        init(queueName);
    }

    /* 初始化 */
    public void init(String queueName) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMqConfig.BROKEN_URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            producer = session.createProducer(queue);
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }

    /* 发送 String 消息 */
    public void sendMsg(String message) {
        try {
            ActiveMQTextMessage msg = new ActiveMQTextMessage();
            msg.setText(message);
            producer.send(msg);
            session.commit();
            LOG.info(Thread.currentThread().getName() + " send a message: {}", msg.getText());
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }

    /* 发送 JSONObject 消息 */
    public void sendMsg(JSONObject jsonObject) {
        try {
            ActiveMQObjectMessage msg = new ActiveMQObjectMessage();
            msg.setObject(jsonObject);
            producer.send(msg);
            session.commit();
            LOG.info(Thread.currentThread().getName() + " send a message: {}", msg.getObject());
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }
}
