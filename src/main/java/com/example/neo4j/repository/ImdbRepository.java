package com.example.neo4j.repository;

import com.example.neo4j.domain.Imdb;
import com.example.neo4j.domain.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImdbRepository extends Neo4jRepository<Imdb, String> {
    List<Imdb> findAll();
}
