package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ShapeTest {
    Shape shape;

    @BeforeEach
    public void init(){
        shape = new Shape();
    }

    private static Stream<Arguments> isConvexPolygonsTestParam(){
        return Stream.of(
                new ShapeTestSetup(new Point(1, 1), new Point(5, 1), new Point(4, 5)),
                new ShapeTestSetup(new Point(679,662), new Point(589,954), new Point(492,850), new Point(-143,86), new Point(238,-788))
        ).map(ShapeTestSetup::getTestArgs);
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("isConvexPolygonsTestParam")
    void isConvex(ShapeTestSetup testShape) {
        // ARRANGE
        shape.setCoordinates(testShape.getShapePoints());
        // ACT
        boolean isConvex = shape.isConvex();
        // ASSERT
        Assertions.assertTrue(isConvex);
    }

    private static Stream<Arguments> isNotConvexPolygonsTestParam(){
        return Stream.of(
                new ShapeTestSetup(new Point(-332,-426), new Point(-845,-616), new Point(865,886), new Point(347,-455)),
                new ShapeTestSetup(new Point(-332,-426), new Point(-845,-616), new Point(865,886), new Point(347,-455), new Point(-470,815), new Point(-744,940), new Point(-805,-418), new Point(563,-184))
        ).map(ShapeTestSetup::getTestArgs);
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("isNotConvexPolygonsTestParam")
    void isNotConvex(ShapeTestSetup testShape) {
        // ARRANGE
        shape.setCoordinates(testShape.getShapePoints());
        // ACT
        boolean isConvex = shape.isConvex();
        // ASSERT
        Assertions.assertFalse(isConvex);
    }
}