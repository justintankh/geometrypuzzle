package com.geometrypuzzle.backend.Shape;

import com.geometrypuzzle.backend.Point.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class ShapeTest {
    Shape shape;

    @BeforeEach
    public void init(){
        shape = new Shape();
    }

    private static Stream<Arguments> isConvexPolygonsTestParam(){
        // ARRANGE
        return Stream.of(
                new ShapeTestSetup(new Point(1, 1), new Point(5, 1), new Point(4, 5)),
                new ShapeTestSetup(new Point(679,662), new Point(589,954), new Point(492,850), new Point(-143,86), new Point(238,-788))
        ).map(ShapeTestSetup::getTestArgs);
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("isConvexPolygonsTestParam")
    void isConvex(ShapeTestSetup testShape) {
        // ACT
        shape.setShape(testShape.getShapePoints());
        // ASSERT
        Assertions.assertEquals(shape.isConvex(), true);
    }

    private static Stream<Arguments> isNotConvexPolygonsTestParam(){
        // ARRANGE
        return Stream.of(
                new ShapeTestSetup(new Point(-332,-426), new Point(-845,-616), new Point(865,886), new Point(347,-455)),
                new ShapeTestSetup(new Point(-332,-426), new Point(-845,-616), new Point(865,886), new Point(347,-455), new Point(-470,815), new Point(-744,940), new Point(-805,-418), new Point(563,-184))
        ).map(ShapeTestSetup::getTestArgs);
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("isNotConvexPolygonsTestParam")
    void isNotConvex(ShapeTestSetup testShape) {
        // ACT
        shape.setShape(testShape.getShapePoints());
        // ASSERT
        Assertions.assertEquals(shape.isConvex(), false);
    }
}