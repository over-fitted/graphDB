package com.example.neo4j.repository;

import com.example.neo4j.domain.CompositeTestNode;
import com.example.neo4j.domain.Imdb;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CompositeTestNodeRepository extends Neo4jRepository<CompositeTestNode, String> {
}
