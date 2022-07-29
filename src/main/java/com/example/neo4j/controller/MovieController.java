package com.example.neo4j.controller;

import com.example.neo4j.domain.Cast;
import com.example.neo4j.domain.Movie;
import com.example.neo4j.domain.PathNode;
import com.example.neo4j.service.BasicCreateService;
import com.example.neo4j.service.BasicSearchService;
import com.example.neo4j.service.BasicUpdateService;
import com.example.neo4j.service.PathSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    BasicSearchService basicSearchService;
    @Autowired
    BasicCreateService basicCreateService;
    @Autowired
    PathSearchService pathSearchService;
    @Autowired
    BasicUpdateService basicUpdateService;

    // returns plot of first movie listing with requested title
    // relevant example is Titanic, multiple possible
    @GetMapping("/plotByTitle/{title}")
    public String findPlotOfMovieByTitle(@PathVariable("title") String title){
        return basicSearchService.findPlotOfMovieByTitle(title).orElse("ERROR: Movie does not exist");
    }

    @PostMapping("/new")
    public ResponseEntity createMovie (@RequestBody Movie movie){
        basicCreateService.createMovie(movie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/update/{old}/{title}")
    public ResponseEntity updateMovieTitle(@PathVariable("old") String old, @PathVariable("title") String title){
        basicUpdateService.updateMovieTitle(old, title);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // Performs regex search on title, case-sensitive
    // Could probably substitute all searches with regex searches
    @GetMapping("/titleRegex/{keyword}")
    public List<Movie> findMovieByTitleRegex(@PathVariable("keyword") String keyword){
        return basicSearchService.findMovieByTitleRegex(keyword.replace('_', ' '));
    }

    @GetMapping("/all")
    public List<Movie> findAllMovie(){
        return basicSearchService.findAllMovie();
    }

    @GetMapping("/shortest/{movie1}/{movie2}")
    public List<Movie> findMovieOnShortestPathBetween(@PathVariable("movie1") String movie1,
                                                    @PathVariable("movie2") String movie2) {
        return basicSearchService.findMovieOnShortestPathBetween(
                movie1.replace('_', ' '),
                movie2.replace('_', ' '));
    }
    @GetMapping("/imdb/id/{id}")
    public Movie findMovieByImdbId(@PathVariable("id") Long id){
        return basicSearchService.findMovieByImdbId(id);
    }
    @GetMapping("/imdb/minRating/{rating}")
    public List<Movie> findMovieByImdbRatingGreaterThanEqual(@PathVariable("rating") Double lowerBound){
        return basicSearchService.findMovieByImdbRatingGreaterThanEqual(lowerBound);
    }
    @GetMapping("/cast/{name}")
    public List<Movie> findMovieByCastName(@PathVariable("name") String name) {
        return basicSearchService.findMovieByCastName(name.replace('_', ' '));
    }

    @GetMapping("/allCast/{start}/{end}/{min}/{max}/{limit}")
    public List<List<String>> allCastPathsBetweenMovies(@PathVariable("start") String start,
                                                        @PathVariable("end") String end,
                                                        @PathVariable("min") int min,
                                                        @PathVariable("max") int max,
                                                        @PathVariable("limit") int limit){
        return pathSearchService.allCastPathsBetweenMovies(start, end, min, max, limit);
    }
}
