package com.bookstore.bookstore.encoder;

import com.bookstore.bookstore.message.UsersMessage;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;

public class UsersMessageEncoder implements Encoder.Text<UsersMessage> {
    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(UsersMessage usersMessage) throws EncodeException {
        StringWriter stringWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = Json.createGenerator(stringWriter)) {
            jsonGenerator.writeStartObject()
                    .write("type", "users")
                    .writeStartArray("userlist");
            for (String user : usersMessage.getUserlist())
                jsonGenerator.write(user);
            jsonGenerator.writeEnd().writeEnd();
        }
        return stringWriter.toString();
    }
}
