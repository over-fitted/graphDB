package com.example.neo4j.service;

import com.example.neo4j.domain.Movie;
import com.example.neo4j.domain.User;
import com.example.neo4j.repository.MovieRepository;
import com.example.neo4j.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BasicUpdateService {
    @Autowired
    private BasicSearchService basicSearchService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    public void updateUserEmail(String id, String email){
        User target = basicSearchService.findUserById(id);
        target.setEmail(email);
        userRepository.save(target);
    }

    public void updateMovieTitle(String old, String title) {
        Movie target = basicSearchService.findMovieByTitleRegex(old).get(0);
        target.setTitle(title);
        movieRepository.save(target);
    }
}
