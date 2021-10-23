package com.bookstore.bookstore.message;

public class JoinMessage extends Message {
    private String name;

    public JoinMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /* For logging purposes */
    @Override
    public String toString() {
        return "[JoinMessage] " + name;
    }
}
