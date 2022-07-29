package com.example.neo4j.controller;

import com.example.neo4j.domain.Movie;
import com.example.neo4j.domain.User;
import com.example.neo4j.service.BasicCreateService;
import com.example.neo4j.service.BasicSearchService;
import com.example.neo4j.service.BasicUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Used to demonstrate creation and updating of nodes
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    BasicSearchService basicSearchService;
    @Autowired
    BasicCreateService basicCreateService;
    @Autowired
    BasicUpdateService basicUpdateService;

    @GetMapping("/all")
    public List<User> findAllUser(){
        return basicSearchService.findAllUser();
    }

    @GetMapping("id/{id}")
    public User findUserById(@PathVariable("id") String id){return basicSearchService.findUserById(id);}

    @PostMapping(value = "/new")
    public ResponseEntity createUser (@RequestBody User user){
        basicCreateService.createUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value="/update/{id}/{email}")
    public ResponseEntity updateUserEmail(@PathVariable("id") String id, @PathVariable("email") String email){
        basicUpdateService.updateUserEmail(id, email);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
