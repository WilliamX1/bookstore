package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.TagNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TagNodeRepository extends Neo4jRepository<TagNode, Long> {
    TagNode findByName(String name);
    List<TagNode> findByRelatedTagNodeName(String name);
}
