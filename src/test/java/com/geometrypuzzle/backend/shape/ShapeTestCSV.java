package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

class ShapeTestCSV {
    Shape shape;

    @BeforeEach
    public void init(){
        shape = new Shape();
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/convexTestTrue.csv")
    void isConvexFromCsv(String input) {
        // Arrange
        List<String> commaSeperated = List.of(input.split(","));
        List<Point> cleanInput = ShapeTestUtils.parseAsPoints(commaSeperated, ":");
        shape.setCoordinates(cleanInput);

        // Act
        boolean isConvex = shape.isConvex();

        // Assert
        Assertions.assertTrue(isConvex);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/convexTestFalse.csv")
    void isNotConvexFromCsv(String input) {
        // Arrange
        List<String> commaSeperated = List.of(input.split(","));
        List<Point> cleanInput = ShapeTestUtils.parseAsPoints(commaSeperated, ":");
        shape.setCoordinates(cleanInput);

        // Act
        boolean isConvex = shape.isConvex();

        // Assert
        Assertions.assertFalse(isConvex);
    }
}