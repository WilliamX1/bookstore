package com.bookstore.bookstore;

//import com.bookstore.bookstore.fulltextsearching.FulltextClient;
import io.spring.guides.gs_producing_web_service.GetFulltextResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

//    @Bean
//    CommandLineRunner lookup(FulltextClient quoteClient) {
//        return args -> {
//            String text = "Computer";
//
//            if (args.length > 0) {
//                text = args[0];
//            }
//            GetFulltextResponse response = quoteClient.getFulltext(text);
//            System.err.println(response.getBookliststr());
//        };
//    }
}
