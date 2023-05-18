package com.geometrypuzzle.backend.Shape;

import com.geometrypuzzle.backend.Point.Point;

import java.util.List;
import java.util.stream.Collectors;

public class ShapeTestUtils {
    public static List<Point> parseAsPoints(List<String> commaSeperated, String delimiter) {
        List<Point> cleanInput = commaSeperated.stream().map((pointsWithDelimiter) -> {
            List<String> coordinates = List.of(pointsWithDelimiter.split(delimiter));
            int x = Integer.parseInt(coordinates.get(0));
            int y = Integer.parseInt(coordinates.get(1));
            return new Point(x, y);
        }).collect(Collectors.toList());
        return cleanInput;
    }
}
