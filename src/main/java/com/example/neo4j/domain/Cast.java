package com.example.neo4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Node
public class Cast extends PathNode{
    private String name;

//    public Cast(String name){
//        super(null,new HashSet<>());
////        this.castList = new ArrayList<>();
//        this.name = name;
//    }


}
