package com.bookstore.bookstore.config;

import org.apache.activemq.ActiveMQConnection;

public class ActiveMqConfig {
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    public static final String BROKEN_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
}
