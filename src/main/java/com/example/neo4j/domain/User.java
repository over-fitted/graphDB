package com.example.neo4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.data.rest.core.Path;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;


@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends PathNode {
    private String name;
    private String email;
    private String password;

    @Relationship(type = "Commented_On", direction = Relationship.Direction.OUTGOING)
    private List<Comment> commentList;
}
