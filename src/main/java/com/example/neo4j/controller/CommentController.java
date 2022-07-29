package com.example.neo4j.controller;

import com.example.neo4j.domain.Comment;
import com.example.neo4j.service.BasicSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Used to show querying edges
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    BasicSearchService basicSearchService;
    @GetMapping("/find/{user}/{movie}")
    public List<Comment> findCommentByUserAndMovie(@PathVariable("user") String user,
                                                   @PathVariable("movie") String movie) {
        return basicSearchService.findCommentByUserAndMovie(
                user.replace('_', ' '),
                movie.replace('_', ' '));
    }
}
