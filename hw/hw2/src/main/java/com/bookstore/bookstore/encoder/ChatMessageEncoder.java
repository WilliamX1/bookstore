package com.bookstore.bookstore.encoder;

import com.bookstore.bookstore.message.ChatMessage;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;

public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {
    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

    @Override
    public String encode(ChatMessage chatMessage) throws EncodeException {
        StringWriter stringWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = Json.createGenerator(stringWriter)) {
            jsonGenerator.writeStartObject()
                    .write("type", "chat")
                    .write("name", chatMessage.getName())
                    .write("target", chatMessage.getTarget())
                    .write("message", chatMessage.getMessage())
                    .writeEnd();
        }
        return stringWriter.toString();
    }
}
