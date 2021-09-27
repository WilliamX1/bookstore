package com.bookstore.bookstore.encoder;

import com.bookstore.bookstore.message.InfoMessage;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.StringWriter;

public class InfoMessageEncoder implements Encoder.Text<InfoMessage> {
    @Override
    public void init(EndpointConfig endpointConfig) {}

    @Override
    public void destroy() {}

    @Override
    public String encode(InfoMessage infoMessage) throws EncodeException {
        StringWriter stringWriter = new StringWriter();
        try (JsonGenerator jsonGenerator = Json.createGenerator(stringWriter)) {
            jsonGenerator.writeStartObject()
                    .write("type", "info")
                    .write("info", infoMessage.getInfo())
                    .writeEnd();
        }
        return stringWriter.toString();
    }
}
