package com.bookstore.bookstore.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("tagNode")
public class TagNode {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private TagNode() {
        // Empty constructor required as of Neo4j API 2.0.5
    };

    public TagNode(String name) {
        this.name = name;
    }

    /**
     * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
     * to ignore the direction of the relationship.
     * https://dzone.com/articlesS/modelling-data-neo4j
     */
    @Relationship(type = "RELATED")
    public Set<TagNode> relatedTagNode = new HashSet<>();;

    public void relatedTo(TagNode tagNode) {
        relatedTagNode.add(tagNode);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
