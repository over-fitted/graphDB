package com.example.neo4j.controller;

import com.example.neo4j.domain.PathNode;
import com.example.neo4j.service.DemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.neo4j.util.StringProcessor.singleSpace;
import static com.example.neo4j.util.StringProcessor.space;

// Stores user stories solutions
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @Operation(
            summary = "K-hops away movies and users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("allPaths/{k}/{username}")
    public List<PathNode> khop(
            @Parameter(description = "length of path", required = true)@PathVariable("k") int k,
            @Parameter(description = "name of source user", required = true)@PathVariable("username") String username){
        return demoService.khop(k, singleSpace(username));
    }

    // Story 1: All paths between all cast nodes
    // UNTESTED - takes too long even if range 1 limit 1
    @Operation(
            summary = "Story 1: All paths between all cast nodes",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "The response for the user request",
                        content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("allPaths/{minRange}/{maxRange}/{limit}")
    public List<Map<String, List<Object>>> allCastPaths(@Parameter(description = "minimum length of path",
                                                            required = true) @PathVariable("minRange") int minRange,
                                                        @Parameter(description = "maximum length of path",
                                                                required = true)@PathVariable("maxRange") int maxRange,
                                                        @Parameter(description = "limit on paths returned",
                                                                required = true)@PathVariable("limit") int limit){
        return demoService.allCastPaths(minRange, maxRange, limit)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 2: traversal with same node class filter
    // 3-node traversal untested, memory insufficient
    @Operation(
            summary = "Story 2: traversal with same node class filter",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/castNames/{minRange}/{maxRange}/{limit}/{castNames}")
    public List<Map<String, List<Object>>> namedCastTraversal(@Parameter(description = "minimum length of path",
                                                                      required = true) @PathVariable("minRange") int minRange,
                                                              @Parameter(description = "maximum length of path",
                                                                      required = true)@PathVariable("maxRange") int maxRange,
                                                              @Parameter(description = "limit on paths returned",
                                                                      required = true)@PathVariable("limit") int limit,
                                                              @Parameter(description = "cast members that must be in each path",
                                                                      required = true)@PathVariable("castNames") List<String> castNames){
        return demoService.namedCastTraversal(minRange, maxRange, limit, space(castNames))
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 3 Mixed node classes (cross-domain) traversals with mixed node class filters
    @Operation(
            summary = "Story 3 Mixed node classes (cross-domain) traversals with mixed node class filters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/anyIds/{minRange}/{maxRange}/{limit}/{nodeIds}")
    public List<Map<String, List<Object>>> namedIdTraversal(@Parameter(description = "minimum length of path",
                                                                    required = true) @PathVariable("minRange") int minRange,
                                                            @Parameter(description = "maximum length of path",
                                                                    required = true)@PathVariable("maxRange") int maxRange,
                                                            @Parameter(description = "limit on paths returned",
                                                                    required = true)@PathVariable("limit") int limit,
                                                            @Parameter(description = "ID of nodes that must be in path",
                                                                    required = true)@PathVariable("nodeIds") List<String> nodeIds){
        return demoService.namedIdTraversal(minRange, maxRange, limit, nodeIds)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 4 Mixed node classes (cross-domain) traversals with nested attributes filter

    // 4a at least x rating score
    // limit refers to number of paths per pair
    // only uses casts edges
    @Operation(
            summary = "Story 4a Gets paths between actors who have starred in movies marked notable by tomato meter",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/nested/tomato/{minRange}/{maxRange}/{limit}/{minTomatoMeter}")
    public List<Map<String, List<Object>>> tomatoNestedTraversal(@Parameter(description = "minimum length of path",
                                                                         required = true)
                                                                     @PathVariable("minRange") int minRange,
                                                                 @Parameter(description = "maximum length of path",
                                                                         required = true)
                                                                     @PathVariable("maxRange") int maxRange,
                                                                 @Parameter(description = "limit on paths returned",
                                                                         required = true)
                                                                     @PathVariable("limit") int limit,
                                                                 @Parameter(description = "minimum tomato score of notable movies",
                                                                         required = true)
                                                                     @PathVariable("minTomatoMeter") double minTomatoMeter){
        return demoService.tomatoNestedTraversal(minRange, maxRange, limit, minTomatoMeter)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // 4b more than x awards
    @Operation(
            summary = "Story 4b Gets paths between actors who have starred in movies marked notable by # awards",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/nested/awards/{minRange}/{maxRange}/{limit}/{minAwards}")
    public List<Map<String, List<Object>>> awardsNestedTraversal(@Parameter(description = "minimum length of path",
                                                                         required = true)
                                                                     @PathVariable("minRange") int minRange,
                                                                 @Parameter(description = "maximum length of path",
                                                                         required = true)
                                                                     @PathVariable("maxRange") int maxRange,
                                                                 @Parameter(description = "limit on paths returned",
                                                                         required = true)
                                                                     @PathVariable("limit") int limit,
                                                                 @Parameter(description = "minimum awards of notable movies",
                                                                         required = true)
                                                                     @PathVariable("minAwards") int minAwards){
        return demoService.awardsNestedTraversal(minRange, maxRange, limit, minAwards)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // 4c imdb rating more than x
    @Operation(
            summary = "Story 4c Gets paths between actors who have starred in movies marked notable by imdb rating",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/nested/rating/{minRange}/{maxRange}/{limit}/{minImdbRating}")
    public List<Map<String, List<Object>>> ratingNestedTraversal(@Parameter(description = "minimum length of path",
            required = true)
                                                                     @PathVariable("minRange") int minRange,
                                                                 @Parameter(description = "maximum length of path",
                                                                         required = true)
                                                                     @PathVariable("maxRange") int maxRange,
                                                                 @Parameter(description = "limit on paths returned",
                                                                         required = true)
                                                                     @PathVariable("limit") int limit,
                                                                 @Parameter(description = "minimum imdb rating of notable movies",
                                                                         required = true)
                                                                 @PathVariable("minImdbRating") int minImdbRating){
        return demoService.ratingNestedTraversal(minRange, maxRange, limit, minImdbRating)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 5 Mixed node classes (cross-domain) traversals with same node class and nested attributes filters
    @Operation(
            summary = "Story 5 Mixed node classes (cross-domain) traversals with same node class and nested attributes filters",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/path/nested/awards/{minRange}/{maxRange}/{limit}/{minAwards}/{castNames}")
    public List<Map<String, List<Object>>> namedCastNestedTraversal(@Parameter(description = "minimum length of path",
                                                                            required = true)
                                                                        @PathVariable("minRange") int minRange,
                                                                    @Parameter(description = "maximum length of path",
                                                                            required = true)
                                                                        @PathVariable("maxRange") int maxRange,
                                                                    @Parameter(description = "limit on paths returned",
                                                                            required = true)
                                                                        @PathVariable("limit") int limit,
                                                                    @Parameter(description = "minimum awards of notable movies",
                                                                            required = true)
                                                                        @PathVariable("minAwards") int minAwards,
                                                                    @Parameter(description = "cast members that must be in each path",
                                                                            required = true)
                                                                        @PathVariable("castNames") List<String> castNames){
        return demoService.namedCastNestedTraversal(minRange, maxRange, limit, minAwards, space(castNames))
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 6
    // Story 1 but only traverse cast and movie nodes
    @Operation(
            summary = "Story 6: All paths between all cast nodes via cast and movie nodes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("movieCastPath/{minRange}/{maxRange}/{limit}")
    public List<Map<String, List<Object>>> allMovieCastPaths(@Parameter(description = "minimum length of path",
            required = true) @PathVariable("minRange") int minRange,
                                                        @Parameter(description = "maximum length of path",
                                                                required = true)@PathVariable("maxRange") int maxRange,
                                                        @Parameter(description = "limit on paths returned",
                                                                required = true)@PathVariable("limit") int limit){
        return demoService.allMovieCastPaths(minRange, maxRange, limit)
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }
    // Story 7a
    // Story 6 but 2 specified nodes
    @Operation(
            summary = "Story 7a: All paths between 2 named cast nodes via cast and movie nodes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("namedCastMovieCastPath/{minRange}/{maxRange}/{limit}/{castA}/{castB}")
    public List<Map<String, List<Object>>> namedCastMovieCastPaths(
            @Parameter(description = "minimum length of path", required = true) @PathVariable("minRange") int minRange,
            @Parameter(description = "maximum length of path", required = true) @PathVariable("maxRange") int maxRange,
            @Parameter(description = "limit on paths returned", required = true) @PathVariable("limit") int limit,
            @Parameter(description = "name of first target cast", required = true) @PathVariable("castA") String castA,
            @Parameter(description = "name of second target cast", required = true) @PathVariable("castB") String castB){
        return demoService.namedCastMovieCastPaths(minRange, maxRange, limit, singleSpace(castA), singleSpace(castB))
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    // Story 7b
    // cast-movie-cast or cast-cast-cast, source and sink specified
    @Operation(
            summary = "Story 7b: All direct paths between 2 named cast nodes via cast and movie nodes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("namedCastDirectPath/{limit}/{castA}/{castB}")
    public List<Map<String, List<Object>>> namedCastDirectPaths(
            @Parameter(description = "limit on paths returned", required = true) @PathVariable("limit") int limit,
            @Parameter(description = "name of first target cast", required = true) @PathVariable("castA") String castA,
            @Parameter(description = "name of second target cast", required = true) @PathVariable("castB") String castB) {
        return demoService.namedCastDirectPaths(limit, singleSpace(castA), singleSpace(castB))
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Story 8: All paths between named cast node and movie node via cast and movie nodes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The response for the user request",
                            content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("namedCastNamedMoviePath/{minRange}/{maxRange}/{limit}/{cast}/{movie}")
    public List<Map<String, List<Object>>> namedCastNamedMoviePath(
            @Parameter(description = "minimum length of path", required = true) @PathVariable("minRange") int minRange,
            @Parameter(description = "maximum length of path", required = true) @PathVariable("maxRange") int maxRange,
            @Parameter(description = "limit on paths returned", required = true) @PathVariable("limit") int limit,
            @Parameter(description = "name of first target cast", required = true) @PathVariable("cast") String cast,
            @Parameter(description = "name of second target cast", required = true) @PathVariable("movie") String movie){
        return demoService.namedCastNamedMoviePath(minRange, maxRange, limit, singleSpace(cast), singleSpace(movie))
                .stream()
                .map(demoService::pathToObjects)
                .collect(Collectors.toList());
    }
}
