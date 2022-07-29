package com.example.neo4j.repository;

import com.example.neo4j.domain.Cast;
import com.example.neo4j.domain.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, String> {
    Optional<Movie> findFirstByTitle(String title);


    @Query("MATCH (n:Movie) WHERE n.plot =~ $keyword RETURN n.title")
    List<Movie> findBySimilarPlot(@Param("keyword") String keyword);

    List<Movie> findMovieByTitleRegex(String title);
    @Query("MATCH p=(m:Movie{title: 'The Matrix'})-[r:Casts]->(c:Cast) return m,collect(r),collect(p)")
    List<Movie> demo();
    @Query("MATCH p=shortestPath((source:Movie {title: $movie1})-[:Casts*]-(sink:Movie {title: $movie2}))\n"
            + "RETURN p"
    )
    List<Movie> findShortestPathBetween(@Param("movie1") String movie1, @Param("movie2") String movie2);

    @Query("MATCH p=shortestPath((source:Movie {title: $movie1})-[:Casts*]-(sink:Movie {title: $movie2}))\n"
            + "WITH collect(p) as paths, source as m\n"
            + "WITH m,\n"
            + "reduce(a=[], node in reduce(b=[], c in [aa in paths | nodes(aa)] | b + c) | case when node in a then a else a + node end) as nodes,\n"
            + "reduce(d=[], relationship in reduce(e=[], f in [dd in paths | relationships(dd)] | e + f) | case when relationship in d then d else d + relationship end) as relationships\n"
            + "return m, relationships, nodes;"
    )
    List<Movie> findShortestPathBetweenReduce(@Param("movie1") String movie1, @Param("movie2") String movie2);
    Movie findByImdbId(Long id);

    List<Movie> findByImdbRatingGreaterThanEqual(Double lowerBound);
    @Query("MATCH p = (m:Movie)-[:Casts]->(c:Cast) WHERE c.name = $name RETURN p")
    List<Movie> findByCastName(@Param("name") String name);
}
