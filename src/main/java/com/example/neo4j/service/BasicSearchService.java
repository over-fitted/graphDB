package com.example.neo4j.service;

import com.example.neo4j.domain.*;
import com.example.neo4j.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BasicSearchService {
    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private CastRepository castRepository;
    private ImdbRepository imdbRepository;
    private PathNodeRepository pathNodeRepository;

    public Optional<String> findPlotOfMovieByTitle(String title){
        return movieRepository.findFirstByTitle(title).map(Movie::getPlot);
    }

    public List<Movie> findAllMovie(){
        return movieRepository.findAll();
    }
    public List<Movie> findMovieByTitleRegex(String keyword){
        return movieRepository.findMovieByTitleRegex(keyword);
    }
    public List<Movie> findMovieOnShortestPathBetween(String movie1, String movie2){
        return movieRepository.findShortestPathBetween(movie1, movie2);
    }
    public Movie findMovieByImdbId(Long id){
        return movieRepository.findByImdbId(id);
    }

    public List<Movie> findMovieByImdbRatingGreaterThanEqual(Double lowerBound){
        return movieRepository.findByImdbRatingGreaterThanEqual(lowerBound);
    }
    public List<Movie> findMovieByCastName(String name) {
        return movieRepository.findByCastName(name);
    }
    public List<User> findAllUser(){return userRepository.findAll();}
    public List<Cast> findAllCast(){
        return castRepository.findAll();
    }
    public List<Cast> findCastOnShortestPathBetween(String person1, String person2){
        return castRepository.findCastOnShortestPathBetween(person1, person2);
    }
    public List<Comment> findCommentByUserAndMovie(String user, String title){
        return userRepository.findByNameAndMovieName(user,title).getCommentList();
    }
    public User findUserById(String id){
        return userRepository.findUserById(id);
    }

    public List<Imdb> findAllImdb(){
        return imdbRepository.findAll();
    }

    public List<PathNode> findAllPathNode(){
        return pathNodeRepository.findAll();
    }

    public List<PathNode> addId() {
        List<Cast> nodes = castRepository.findAll();
        castRepository.saveAll(nodes);
        return null;
    }
    public List<PathNode> custom(){
        return pathNodeRepository.demo();
    }

    public List<Movie> movieCustom(){
        return movieRepository.demo();
    }

    public List<Imdb> addImdbId() {
        imdbRepository.saveAll(imdbRepository.findAll());
        return null;
    }

    public List<PathNode> shortestCastsPathCrossDomain(String first, String second) {
        return pathNodeRepository.shortestCastsPathCrossDomain(first, second);
    }

    public List<PathNode> enumerateAllPaths(String first, String second, int maxLength) {
        return pathNodeRepository.findAllPaths(first, second, maxLength);
    }

}
