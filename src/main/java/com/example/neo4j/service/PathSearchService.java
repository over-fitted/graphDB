package com.example.neo4j.service;

import com.example.neo4j.repository.PathNodeRepository;
import lombok.AllArgsConstructor;
import org.neo4j.cypherdsl.core.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PathSearchService {
    PathNodeRepository pathNodeRepository;

    public List<List<String>> allCastPathsBetweenMovies(String start, String end, int min, int max, int limit) {
        return pathNodeRepository.allCastPathsBetweenMovies(start, end, min, max, limit)
                .stream().map(x-> Arrays.asList(x.split(", "))).collect(Collectors.toList());
    }

    public List<List<String>> allPathsBetweenNodesShowEdges(String start, String end, int min, int max, int limit) {
        return pathNodeRepository.allPathsBetweenNodesShowEdges(start, end, min, max, limit)
                .stream().map(x-> Arrays.asList(x.split(", "))).collect(Collectors.toList());
    }

//    public Collection<Object> castPathsBetweenMultipleNodes(List<String> nodeIds, int minLen, int maxLen, int lim){
//        return pathNodeRepository.findAll(castPathsBetweenMultipleNodesStatement(nodeIds, minLen, maxLen, lim));
//    }

    // use .unbounded on relationship length for *
    // https://neo4j-contrib.github.io/cypher-dsl/current/project-info/apidocs/org/neo4j/cypherdsl/core/ExposesPatternLengthAccessors.html#unbounded()
    // RelationshipPattern is base class for Relationship and RelationshipChain
    // https://neo4j-contrib.github.io/cypher-dsl/current/project-info/apidocs/org/neo4j/cypherdsl/core/RelationshipPattern.html
    public Statement castPathsBetweenMultipleNodesStatement(List<String> nodeIds, int minLen, int maxLen, int lim){
        ArrayList<Node> targets = new ArrayList<>();
        for(String id: nodeIds){
            targets.add(Cypher.node("PathNode")
                    .withProperties("id", Cypher.literalOf(id)));
        }
        RelationshipPattern base = targets.get(0)
                .relationshipBetween(targets.get(1), "Casts")
                .length(minLen, maxLen);
        for(int i = 2; i < targets.size(); i++) {
            base = base.relationshipBetween(targets.get(i), "Casts")
                    .length(minLen, maxLen);
        }
        NamedPath path = Cypher.path("p").definedBy(base);
        Node n = Cypher.node("PathLabel").named("n");
        SymbolicName acc = Cypher.name("acc");
        var nodeReduction = Functions.reduce(n.getRequiredSymbolicName())
                .in(Functions.nodes(path)).map(acc.add(n.property("id")).add(Cypher.literalOf(", "))).accumulateOn(acc).withInitialValueOf(Cypher.literalOf(""));
        return Cypher.match(path).returning(nodeReduction).limit(lim).build();
    }


}
