package com.bookstore.bookstore.decoder;

import com.bookstore.bookstore.message.*;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.util.*;

public class MessageDecoder implements Decoder.Text<Message> {
    /* Store the name-value pairs from a JSON message as a Map */
    private Map<String, String> messageMap;

    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

    /* Create a new Message object if the message can be decoded */
    @Override
    public Message decode(String string) throws DecodeException {
        System.out.println(string);
        Message msg = null;
        if (willDecode(string)) {
            switch (messageMap.get("type")) {
                case "chat":
                    msg = new ChatMessage(messageMap.get("name"),
                            messageMap.get("target"),
                            messageMap.get("message"));
                    break;
                case "info":
                    msg = new InfoMessage(messageMap.get("info"));
                    break;
                case "join":
                    msg = new JoinMessage(messageMap.get("name"));
                    break;
                case "users":
                    msg = new UsersMessage(Collections.singletonList(messageMap.get("userlist")));
                    break;
            }
        } else {
            throw new DecodeException(string, "[Message] can't decode");
        }
        return msg;
    }

    /* Decode a JSON message into a Map and check if it contains
    * all the required fields according to its type */
    @Override
    public boolean willDecode(String string) {
        boolean decodes = false;
        /* Convert the mesage into a map */
        messageMap = new HashMap<>();
        JsonParser jsonParser = Json.createParser(new StringReader(string));
        while (jsonParser.hasNext()) {
            if (jsonParser.next() == JsonParser.Event.KEY_NAME) {
                String key = jsonParser.getString();
                jsonParser.next();
                String value = jsonParser.getString();
                messageMap.put(key, value);
            }
        }
        /* Check the kind of message and if all fields are included */
        Set keys = messageMap.keySet();
        if (keys.contains("type")) {
            switch (messageMap.get("type")) {
                case "chat":
                    String[] charMsgKeys = {"name", "target", "message"};
                    if (keys.containsAll(Arrays.asList(charMsgKeys))) decodes = true;
                    break;
                case "info":
                    if (keys.contains("info")) decodes = true;
                    break;
                case "join":
                    if (keys.contains("name")) decodes = true;
                    break;
                case "users":
                    if (keys.contains("userlist")) decodes = true;
                    break;
            }
        }
        return decodes;
    }
}
