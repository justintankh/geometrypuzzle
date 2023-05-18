package com.geometrypuzzle.backend.Shape;

import com.geometrypuzzle.backend.Point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

class ShapeTestFromCSV {
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

        // Act
        shape.setShape(cleanInput);

        // Assert
        Assertions.assertEquals(shape.isConvex(), true);
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/convexTestFalse.csv")
    void isNotConvexFromCsv(String input) {
        // Arrange
        List<String> commaSeperated = List.of(input.split(","));
        List<Point> cleanInput = ShapeTestUtils.parseAsPoints(commaSeperated, ":");

        // Act
        shape.setShape(cleanInput);

        // Assert
        Assertions.assertEquals(shape.isConvex(), false);
    }
}