package com.example.neo4j.repository;

import com.example.neo4j.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {
    @Query("MATCH p=(u:User)-[c:Commented_On]->(m:Movie) WHERE u.name=~$name AND m.title=~$title\n"
            + "RETURN p")
    User findByNameAndMovieName(
            @Param("name") String name,
            @Param("title") String title);

//    @Query("MATCH p=(u:User) WHERE u.id = $id RETURN p")
    User findUserById(@Param("id") String id);
}
