package com.example.neo4j.repository;

import com.example.neo4j.domain.Cast;
import com.example.neo4j.domain.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastRepository extends Neo4jRepository<Cast, String> {
    List<Cast> findAll();
    @Query(""
            + "MATCH p=shortestPath((:Cast {name: $person1})-[:Casts*]-(:Cast {name: $person2}))\n"
            + "RETURN p"
    )
    <T> List<T> findCastOnShortestPathBetween(@Param("person1") String person1, @Param("person2") String person2);
}
