package com.example.neo4j.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringProcessor {
    public static List<String> space(List<String> target){
        return target.stream().map(StringProcessor::singleSpace)
                .collect(Collectors.toList());
    }

    public static String singleSpace(String target){
        return String.join(" ", Arrays.asList(target.split("_")));
    }

    public static List<List<String>> stringToList(List<String> paths){
        return paths.stream().map(x->Arrays.asList(x.split(", "))).collect(Collectors.toList());
    }
}
