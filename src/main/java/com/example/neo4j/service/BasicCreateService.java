package com.example.neo4j.service;

import com.example.neo4j.domain.Movie;
import com.example.neo4j.domain.User;
import com.example.neo4j.repository.MovieRepository;
import com.example.neo4j.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class BasicCreateService {
    private MovieRepository movieRepository;
    private UserRepository userRepository;

        public void createMovie (Movie movie){
            movieRepository.save(movie);
        }

    public void createUser(User user){
        userRepository.save(user);
    }
}
