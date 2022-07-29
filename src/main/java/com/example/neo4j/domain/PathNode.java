package com.example.neo4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Set;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathNode {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @DynamicLabels
    public Set<String> labels;

}
