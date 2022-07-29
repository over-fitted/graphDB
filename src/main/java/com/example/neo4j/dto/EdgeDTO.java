package com.example.neo4j.dto;

import lombok.Value;

@Value
public class EdgeDTO {
    public enum Direction {
        LEFT,
        RIGHT
    }
    private String label;
    private Direction direction;
}
