package com.bookstore.bookstore;

//import com.bookstore.bookstore.fulltextsearching.FulltextClient;
import com.bookstore.bookstore.entity.TagName;
import com.bookstore.bookstore.entity.TagNode;
import com.bookstore.bookstore.repository.TagNameRepository;
import com.bookstore.bookstore.repository.TagNodeRepository;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableNeo4jRepositories
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public Connector connector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8787);
        connector.setSecure(false);
        connector.setRedirectPort(9090);
        return connector;
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector) {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(connector);
        return tomcatServletWebServerFactory;
    }

    @Bean
    CommandLineRunner createTagNodeAndConnect(TagNodeRepository tagNodeRepository, TagNameRepository tagNameRepository) {
        return args -> {

            tagNodeRepository.deleteAll();

            List<TagName> tagNameList = tagNameRepository.findAll();

            List<TagNode> tagNodeList = new ArrayList<>();

            for (TagName tagName : tagNameList) {
                TagNode tagNode = new TagNode(tagName.getName());
                tagNodeRepository.save(tagNode);
                tagNodeList.add(tagNode);
            }

            int n = tagNodeList.size();
            Random random = new Random();

            for (int i = 0; i < 2 * n; i++) {
                int start = random.nextInt(n - 1) + 1;
                int end = random.nextInt(n - 1) + 1;

                if (start == end) continue;

                TagNode start_node = tagNodeList.get(start);
                TagNode end_node = tagNodeList.get(end);

                if (start_node != null && end_node != null) {
                    start_node.relatedTo(end_node);
                    tagNodeRepository.save(start_node);
                }

            }
        };
    }
}
