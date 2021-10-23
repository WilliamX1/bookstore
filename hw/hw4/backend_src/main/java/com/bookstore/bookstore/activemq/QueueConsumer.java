package com.bookstore.bookstore.activemq;

import com.bookstore.bookstore.config.ActiveMqConfig;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class QueueConsumer {
    private final Logger LOG = LoggerFactory.getLogger(QueueConsumer.class);

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public QueueConsumer(String queueName) {
        init(queueName);
    }

    /* 初始化 */
    public void init(String queueName) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMqConfig.BROKEN_URL);
            connectionFactory.setTrustAllPackages(true);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            consumer = session.createConsumer(queue);
        } catch (JMSException e) {
            LOG.error("" + e);
        }
    }

    /* 获取 consumer */
    public MessageConsumer getConsumer() {
        return consumer;
    }

    /* 获取 LOG */
    public Logger getLog() {
        return LOG;
    }
}
