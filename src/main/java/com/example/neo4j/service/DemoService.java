package com.example.neo4j.service;

import com.example.neo4j.domain.PathNode;
import com.example.neo4j.dto.EdgeDTO;
import com.example.neo4j.repository.PathNodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.neo4j.dto.EdgeDTO.Direction.LEFT;
import static com.example.neo4j.dto.EdgeDTO.Direction.RIGHT;
import static com.example.neo4j.util.StringProcessor.stringToList;

@Service
@AllArgsConstructor
public class DemoService {
    PathNodeRepository pathNodeRepository;

    // takes in path of node - edge - node pattern, converts to corresponding node/edgeDTO
    public Map<String, List<Object>> pathToObjects(List<String> path){
        return path.stream()
                .map(x->convert(x))
                .collect(Collectors.groupingBy(x->x instanceof EdgeDTO ? "Edge" : "Node"));
    }

    public Object convert(String target){
        switch(target.charAt(target.length()-1)){
            case '<':
                return new EdgeDTO(target.substring(0,target.length()-1), LEFT);
            case '>':
                return new EdgeDTO(target.substring(0,target.length()-1), RIGHT);
        }
        return pathNodeRepository.findById(target);
    }

    public List<List<String>> namedCastTraversal(int minRange, int maxRange, int limit, List<String> castNames) {
        return stringToList(pathNodeRepository.castTraversal(minRange, maxRange, limit, castNames));
    }

    public List<List<String>> allCastPaths(int minRange, int maxRange, int limit) {
        return stringToList(pathNodeRepository.allCastPaths(minRange, maxRange, limit));
    }

    public List<List<String>> namedIdTraversal(int minRange, int maxRange, int limit, List<String> nodeIds) {
        return stringToList(pathNodeRepository.namedIdTraversal(minRange, maxRange, limit, nodeIds));
    }

    public List<List<String>> tomatoNestedTraversal(int minRange, int maxRange, int limit, double minTomatoMeter) {
        return stringToList(pathNodeRepository.tomatoNestedTraversal(minRange, maxRange, limit, minTomatoMeter));
    }

    public List<List<String>> awardsNestedTraversal(int minRange, int maxRange, int limit, int minAwards) {
        return stringToList(pathNodeRepository.awardsNestedTraversal(minRange, maxRange, limit, minAwards));
    }

    public List<List<String>> ratingNestedTraversal(int minRange, int maxRange, int limit, int minImdbRating) {
        return stringToList(pathNodeRepository.ratingNestedTraversal(minRange, maxRange, limit, minImdbRating));
    }

    public List<List<String>> namedCastNestedTraversal(int minRange, int maxRange,
                                                       int limit, int minAwards, List<String> castNames) {
        return stringToList(pathNodeRepository.namedCastNestedTraversal(minRange, maxRange, limit, minAwards,
                                                                        castNames));
    }

    public List<List<String>> allMovieCastPaths(int minRange, int maxRange, int limit) {
        return stringToList(pathNodeRepository.allMovieCastPaths(minRange, maxRange, limit));
    }

    public List<List<String>> namedCastMovieCastPaths(int minRange, int maxRange, int limit, String castA, String castB) {
        return stringToList(pathNodeRepository.namedCastMovieCastPaths(minRange, maxRange, limit, castA, castB));
    }

    public List<List<String>> namedCastDirectPaths(int limit, String castA, String castB) {
        return stringToList(pathNodeRepository.namedCastDirectPaths(limit, castA, castB));
    }

    public List<List<String>> namedCastNamedMoviePath(int minRange, int maxRange, int limit, String cast, String movie) {
        return stringToList(pathNodeRepository.namedCastNamedMoviePath(minRange, maxRange, limit, cast, movie));
    }

    public List<PathNode> khop(int k, String username) {
        return pathNodeRepository.khop(k, username);
    }
}
