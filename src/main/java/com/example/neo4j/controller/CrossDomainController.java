package com.example.neo4j.controller;

import com.example.neo4j.domain.Cast;
import com.example.neo4j.domain.Movie;
import com.example.neo4j.domain.PathNode;
import com.example.neo4j.service.BasicSearchService;
import com.example.neo4j.service.PathSearchService;
import org.neo4j.cypherdsl.core.Statement;
import org.neo4j.driver.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// hard-coded source-sink solutions + deprecated cypherdsl
@RestController
@RequestMapping("/path")
public class CrossDomainController {
    @Autowired
    private BasicSearchService basicSearchService;
    @Autowired
    private PathSearchService pathSearchService;

    @GetMapping("/all")
    public List<PathNode> findAllPathNodes(){
        return basicSearchService.findAllPathNode();
    }


    @GetMapping("/shortest/casts/{first}/{second}")
    public List<PathNode> shortestCastsPathCrossDomain(@PathVariable("first") String first, @PathVariable("second") String second){
        return basicSearchService.shortestCastsPathCrossDomain(first, second);
    }

    @GetMapping("/enum/{maxLength}/{first}/{second}")
    public List<PathNode> enumerateAllPaths(@PathVariable("first") String first,
                                            @PathVariable("second") String second,
                                            @PathVariable("maxLength") int maxLength){
        return basicSearchService.enumerateAllPaths(first, second, maxLength);
    }

    // Hard-coded source and sink
    @GetMapping("/allPath/{start}/{end}/{min}/{max}/{limit}")
    public List<List<String>> allPathsBetweenNodes(@PathVariable("start") String start,
                                                        @PathVariable("end") String end,
                                                        @PathVariable("min") int min,
                                                        @PathVariable("max") int max,
                                                        @PathVariable("limit") int limit){
        return pathSearchService.allPathsBetweenNodesShowEdges(start, end, min, max, limit);
    }

    // DEPRECATED: PoC of CypherDSL, abandoned in favour of list comprehension
    @GetMapping("/casts/{minRange}/{maxRange}/{limit}/{nodeIds}")
    public Statement generateCastsPathQuery(@PathVariable("minRange") int min,
                                                       @PathVariable("maxRange") int max,
                                                       @PathVariable("limit") int limit,
                                                       @PathVariable("nodeIds") ArrayList<String> nodeIds){
        return pathSearchService.castPathsBetweenMultipleNodesStatement(nodeIds, min, max, limit);
    }
}
