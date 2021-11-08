package com.bookstore.bookstore.fulltextsearching;

import com.alibaba.fastjson.JSONObject;
import com.bookstore.bookstore.dao.BookDao;
import io.spring.guides.gs_producing_web_service.GetFulltextRequest;
import io.spring.guides.gs_producing_web_service.GetFulltextResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class FulltextEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private BookDao bookDao;

    @Autowired
    public FulltextEndpoint(BookDao bookDao) { this.bookDao = bookDao; }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getFulltextRequest")
    @ResponsePayload
    public GetFulltextResponse getFulltext(@RequestPayload GetFulltextRequest request) {
        GetFulltextResponse response = new GetFulltextResponse();
        response.setBookliststr(JSONObject.toJSONString(bookDao.fulltextSearchBook(request.getText())));

        return response;
    }
}
