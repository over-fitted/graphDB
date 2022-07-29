package com.example.neo4j.controller;

import com.example.neo4j.domain.Cast;
import com.example.neo4j.domain.CompositeTestNode;
import com.example.neo4j.domain.Movie;
import com.example.neo4j.repository.CastRepository;
import com.example.neo4j.repository.CompositeTestNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comp")
public class CompositeTestController {
    @Autowired
    private CompositeTestNodeRepository compositeTestNodeRepository;
    @Autowired
    private CastRepository castRepository;
    @PostMapping("/new")
    public ResponseEntity createComp (@RequestBody CompositeTestNode compositeTestNode){
        compositeTestNodeRepository.save(compositeTestNode);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
