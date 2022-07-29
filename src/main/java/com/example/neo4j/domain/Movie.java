package com.example.neo4j.domain;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Node
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends PathNode{
    private String title;
    private String plot;
    @Property("num_mflix_comments")
    private Integer numMflixComments;
    @Property("lastupdated")
    private Long lastUpdated;
    @Property("tomatoes_meter")
    private Integer tomatoesMeter;
    @Property("tomatoes_rating")
    private Double tomatoesRating;
    @Property("tomatoes_numReviews")
    private Integer tomatoes_numReviews;
    @Property("awards")
    private String awards;

    @Relationship(type = "Directed_By", direction = Direction.OUTGOING)
    private List<Director> directorList;

    @Relationship(type = "Written_By", direction = Direction.OUTGOING)
    private List<Writer> writerList;

    @Relationship(type = "Categorized_As", direction = Direction.OUTGOING)
    private List<Genre> genreList;

    @Relationship(type = "Rated_As", direction = Direction.OUTGOING)
    private Imdb imdb;

    @Relationship(type = "Casts", direction = Direction.OUTGOING)
    private List<Cast> castList;
}
