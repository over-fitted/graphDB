package com.example.neo4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.Map;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompositeTestNode {
    @Id @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    @CompositeProperty
    private Map<String, Object> awards;
    public void addItem(String key, Object val){
        awards.put(key,val);
    }
}
