//package com.bookstore.bookstore.fulltextsearching;
//
//import io.spring.guides.gs_producing_web_service.GetFulltextRequest;
//import io.spring.guides.gs_producing_web_service.GetFulltextResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
//import org.springframework.ws.soap.client.core.SoapActionCallback;
//
//@Slf4j
//public class FulltextClient extends WebServiceGatewaySupport {
//
//    public GetFulltextResponse getFulltext(String text) {
//        GetFulltextRequest request = new GetFulltextRequest();
//        request.setText(text);
//
//        log.info("Requesting full-text search for " + text);
//
//        GetFulltextResponse response = (GetFulltextResponse) getWebServiceTemplate()
//                .marshalSendAndReceive("http://localhost:9090/ws/fulltext", request,
//                        new SoapActionCallback(
//                                "http://spring.io/guides/gs-producing-web-service/GetFulltextRequest"
//                        ));
//
//        return response;
//    }
//}
