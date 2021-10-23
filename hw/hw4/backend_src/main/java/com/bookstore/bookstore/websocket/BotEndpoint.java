package com.bookstore.bookstore.websocket;

import com.bookstore.bookstore.decoder.MessageDecoder;
import com.bookstore.bookstore.encoder.ChatMessageEncoder;
import com.bookstore.bookstore.encoder.InfoMessageEncoder;
import com.bookstore.bookstore.encoder.JoinMessageEncoder;
import com.bookstore.bookstore.encoder.UsersMessageEncoder;
import com.bookstore.bookstore.message.*;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Websocket endpoint */
@ServerEndpoint(
        value = "/websocketbot",
        decoders = {MessageDecoder.class},
        encoders = {ChatMessageEncoder.class, InfoMessageEncoder.class,
                JoinMessageEncoder.class, UsersMessageEncoder.class}
)
@Component
/* This is a BotEndpoint instance per connection */
public class BotEndpoint {
    private static final Logger logger = Logger.getLogger("BotEndpoint");
    private static Queue<Session> mySession = new ConcurrentLinkedDeque<>();

    @OnOpen
    public void openConnection(Session session) {
        mySession.add(session);
        logger.log(Level.INFO, "Connection opened.");
    }

    @OnMessage
    public void message(final Session session, Message msg) {
        logger.log(Level.INFO, "Received: {0}", msg.toString());

        if (msg instanceof JoinMessage) {
            /* Add the new user and notify everybody */
            JoinMessage joinMessage = (JoinMessage) msg;
            session.getUserProperties().put("name", joinMessage.getName());
            session.getUserProperties().put("active", true);
            logger.log(Level.INFO, "Received: {0}", joinMessage.toString());
            sendAll(session, new InfoMessage(joinMessage.getName() + " has joined the chat."));
            sendAll(session, new UsersMessage(this.getUserList(session)));
        } else if (msg instanceof ChatMessage) {
            ChatMessage chatMessage = (ChatMessage) msg;
            session.getUserProperties().put("name", chatMessage.getName());
            session.getUserProperties().put("target", chatMessage.getTarget());
            session.getUserProperties().put("message", chatMessage.getMessage());
            logger.log(Level.INFO, "Received: {0}", chatMessage.toString());
            if (chatMessage.getTarget().equals("all"))
                sendAll(session, new ChatMessage(chatMessage.getName(), chatMessage.getTarget(), chatMessage.getMessage()));
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Notify everybody */
        session.getUserProperties().put("active", false);
        if (session.getUserProperties().containsKey("name")) {
            String name = session.getUserProperties().get("name").toString();
            sendAll(session, new InfoMessage(name + " has left the chat"));

            mySession.remove(session);
            sendAll(session, new UsersMessage(this.getUserList(session)));
        }
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable throwable) {
        logger.log(Level.INFO, "Connection error {0}", throwable.toString());
    }

    /* Forward a message to all connected clients
     * The endpoint figures what encoder to use based on the message type */
    public synchronized void sendAll(Session session, Object msg) {
        try {
            for (Session s : mySession) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(msg);
                    logger.log(Level.INFO, "Sent {0}", msg.toString());
                }
            }
        } catch (IOException | EncodeException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    /* Returns the list of users from the properties of all open sessions */
    public List<String> getUserList(Session session) {
        List<String> users = new ArrayList<>();
        for (Session s : mySession) {
            if (s.isOpen() && (boolean) s.getUserProperties().get("active")
                    && !users.contains(s.getUserProperties().get("name").toString())) // 防止重复添加
                users.add(s.getUserProperties().get("name").toString());
        }
        return users;
    }
}
