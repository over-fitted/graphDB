package com.example.neo4j.repository;

import com.example.neo4j.domain.PathNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.neo4j.repository.support.CypherdslConditionExecutor;
import org.springframework.data.neo4j.repository.support.CypherdslStatementExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PathNodeRepository extends Neo4jRepository<PathNode, String>,
        CypherdslConditionExecutor<PathNode>,
        CypherdslStatementExecutor<PathNode> {

    @Query("MATCH p=(source{id:$first})-[*..3]-(sink{id:$second}) RETURN p")
    List<PathNode> findAllPaths(@Param("first") String first,
                                       @Param("second") String second,
                                      int maxLength);

    @Query("MATCH p=shortestPath((source{id:$first})-[:Casts*]-(sink{id:$second}))\n"
            + "RETURN p"
    )
    List<PathNode> shortestCastsPathCrossDomain(@Param("first") String first,
                                                @Param("second") String second);

    @Query("MATCH (m:Movie{title: 'The Matrix'})<-[r:Commented_On]-(p:User) return m,collect(r),collect(p)")
    List<PathNode> demo();

    // Demonstrates apoc.path.expandConfig, and apoc.text.join to return only node IDs
    @Query("MATCH (m:Movie) WHERE m.title = $first\n" +
            "MATCH (n:Movie) WHERE n.title = $second\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    beginSequenceAtStart:true,\n" +
            "    sequence: 'Movie, Casts, Cast, Casts',\n" +
            "    minLevel: $minLevel,\n" +
            "    maxLevel: $maxLevel,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit\n" +
            "})\n" +
            "yield path as p\n" +
            "RETURN apoc.text.join([n in nodes(p) | n.id], ', ')")
    List<String> allCastPathsBetweenMovies(@Param("first") String first,
                                           @Param("second") String second,
                                           @Param("minLevel") int minLevel,
                                           @Param("maxLevel") int maxLevel,
                                           @Param("limit") int limit);

    // Demonstrates using reduce to return both nodes and edges in the same string
    @Query("MATCH (m) WHERE m.id = $first\n" +
            "MATCH (n) WHERE n.id = $second\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minLevel,\n" +
            "    maxLevel: $maxLevel,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit\n" +
            "})\n" +
            "yield path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', '," +
            "i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) + " +
            "CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END + " +
            "', ' + nodes(p)[i].id + ', ')")
    List<String> allPathsBetweenNodesShowEdges(@Param("first") String first,
                                               @Param("second") String second,
                                               @Param("minLevel") int minLevel,
                                               @Param("maxLevel") int maxLevel,
                                               @Param("limit") int limit);

    /**
     * --------------
     * User Stories
     * --------------
     */

    // Story 1

    @Query("MATCH (m:Cast)\n" +
            "MATCH (n:Cast)\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n"
            + "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> allCastPaths(@Param("minRange") int minRange, @Param("maxRange") int maxRange,
                              @Param("limit") int limit);

    // Story 2 Mixed node classes (cross-domain) traversals with same node class filters
    @Query("MATCH (m:Cast) WHERE m.name in $castNames\n" +
            "MATCH (n:Cast) WHERE n.name in $castNames\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,   \n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "WHERE ALL(target in $castNames WHERE target in [n in nodes(p) WHERE 'Cast' in labels(n) | n.name])\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n"
            + "+ ', ' + nodes(p)[i].id + ', ')")
    List<String> castTraversal(@Param("minRange") int minRange,
                               @Param("maxRange") int maxRange,
                               @Param("limit") int limit,
                               @Param("castNames") List<String> castNames);

    // Story 3 Mixed node classes (cross-domain) traversals with mixed node class filters
    @Query("MATCH (m) WHERE m.id in $targetIds\n" +
            "MATCH (n) WHERE n.id in $targetIds\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "WHERE ALL(target in $targetIds WHERE target in [n in nodes(p) | n.id])\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n"
            + "+ ', ' + nodes(p)[i].id + ', ')")
    List<String> namedIdTraversal(@Param("minRange") int minRange,
                              @Param("maxRange") int maxRange,
                              @Param("limit") int limit,
                              @Param("targetIds") List<String> targetIds);

    // 4a at least x rating score
    @Query("MATCH (n:Cast)-[:Casts]-(m:Movie)\n" +
            "WHERE m.tomatoes_meter >= $minTomatoMeter\n" +
            "WITH collect(n) as res\n" +
            "MATCH (a:Cast) WHERE a in res\n" +
            "MATCH (b:Cast) WHERE b in res\n" +
            "CALL apoc.path.expandConfig(a, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes: [b],\n" +
            "    relationshipFilter: \"Casts\",\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n"
            + "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> tomatoNestedTraversal(@Param("minRange") int minRange,
                                       @Param("maxRange") int maxRange,
                                       @Param("limit") int limit,
                                       @Param("minTomatoMeter") double minTomatoMeter);


    // 4b at least x awards
    @Query("MATCH (n:Cast)-[:Casts]-(m:Movie)\n" +
            "WHERE apoc.convert.fromJsonMap(m.awards).wins >= $minAwards\n" +
            "WITH collect(n) as res\n" +
            "MATCH (a:Cast) WHERE a in res\n" +
            "MATCH (b:Cast) WHERE b in res\n" +
            "CALL apoc.path.expandConfig(a, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes: [b],\n" +
            "    relationshipFilter: \"Casts\",\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> awardsNestedTraversal(@Param("minRange") int minRange,
                                       @Param("maxRange") int maxRange,
                                       @Param("limit") int limit,
                                       @Param("minAwards") int minAwards);

    @Query("MATCH (m:Movie)-[:Rated_As]->(i:Imdb) WHERE i.rating >= $minImdbRating\n" +
            "MATCH (n:Cast)<-[:Casts]-(m)\n" +
            "WITH collect(n) as res\n" +
            "MATCH (a:Cast) WHERE a in res\n" +
            "MATCH (b:Cast) WHERE b in res\n" +
            "CALL apoc.path.expandConfig(a, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes: [b],\n" +
            "    relationshipFilter: \"Casts\",\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> ratingNestedTraversal(@Param("minRange") int minRange,
                                       @Param("maxRange") int maxRange,
                                       @Param("limit") int limit,
                                       @Param("minImdbRating") int minImdbRating);

    @Query("MATCH (a:Cast) WHERE a.name in $castNames\n" +
            "MATCH (b:Cast) WHERE b.name in $castNames\n" +
            "CALL apoc.path.expandConfig(a, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes: [b],\n" +
            "    relationshipFilter: \"Casts\",\n" +
            "    limit: $limit\n" +
            "})\n" +
            "YIELD path as p\n" +
            "WHERE all(no in [n in nodes(p) WHERE 'Movie' in labels(n)] WHERE apoc.convert.fromJsonMap(no.awards).wins >= $minAwards)\n" +
            "AND ALL(target in $castNames WHERE target in [n in nodes(p) | n.name])\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')")
    List<String> namedCastNestedTraversal(@Param("minRange") int minRange,
                                          @Param("maxRange") int maxRange,
                                          @Param("limit") int limit,
                                          @Param("minAwards") int minAwards,
                                          @Param("castNames") List<String> castNames);

    @Query("MATCH (m:Cast)\n" +
            "MATCH (n:Cast)\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit,\n" +
            "    nodeFilter: 'Movie, Cast'\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> allMovieCastPaths(@Param("minRange") int minRange, @Param("maxRange") int maxRange,
                              @Param("limit") int limit);

    @Query("MATCH (m:Cast{name:$castA})\n" +
            "MATCH (n:Cast{name:$castB})\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit,\n" +
            "    nodeFilter: 'Movie, Cast'\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> namedCastMovieCastPaths(@Param("minRange") int minRange,
                                         @Param("maxRange") int maxRange,
                                         @Param("limit") int limit,
                                         @Param("castA") String castA,
                                         @Param("castB") String castB);

    @Query("MATCH p=(m:Cast{name:$castA})--(k)--(n:Cast{name:$castB})\n" +
            "WHERE k:Movie or k:Cast\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')")
    List<String> namedCastDirectPaths(int limit, String castA, String castB);

    @Query("MATCH (m:Movie{title:$movie})\n" +
            "MATCH (n:Cast{name:$cast})\n" +
            "CALL apoc.path.expandConfig(m, {\n" +
            "    minLevel: $minRange,\n" +
            "    maxLevel: $maxRange,\n" +
            "    terminatorNodes:[n],\n" +
            "    limit: $limit,\n" +
            "    nodeFilter: 'Movie, Cast'\n" +
            "})\n" +
            "YIELD path as p\n" +
            "return reduce(b=nodes(p)[0].id + ', ',i in range(1,length(p)) | b + TYPE(relationships(p)[i-1]) \n" +
            "+ CASE startnode(relationships(p)[i-1]) WHEN nodes(p)[i-1] THEN '>' ELSE '<' END \n" +
            "+ ', ' + nodes(p)[i].id + ', ')\n" +
            "LIMIT 15")
    List<String> namedCastNamedMoviePath(int minRange, int maxRange, int limit, String cast, String movie);

    @Query("MATCH (m:User{name:$username})\n" +
            "CALL apoc.neighbors.byhop(m,\"Casts|Commented_On|Directed_By|Rated_As|Written_By\" ,$k)\n" +
            "YIELD nodes\n" +
            "return nodes")
    List<PathNode> khop(int k, String username);
}