//package com.bookstore.bookstore.fulltextsearching;
//
//import io.spring.guides.gs_producing_web_service.GetFulltextResponse;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.oxm.jaxb.Jaxb2Marshaller;
//
//@Configuration
//public class CountryConfiguration {
//
//    @Bean
//    public Jaxb2Marshaller marshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        /* this package must match the package in the <generatePackage> specified in pom.xml */
//        marshaller.setContextPath("com.bookstore.bookstore.wsdl");
//        return marshaller;
//    }
//
//    @Bean
//    public FulltextClient fulltextClient(Jaxb2Marshaller marshaller) {
//        FulltextClient client = new FulltextClient();
//        client.setDefaultUri("http://localhost:9090/ws");
//        client.setMarshaller(marshaller);
//        client.setUnmarshaller(marshaller);
//        return client;
//    }
//}
