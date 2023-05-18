package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ShapeTest {
    Shape shapeUnderTest;

    @BeforeEach
    public void init() {
        shapeUnderTest = new Shape();
    }

    private static Stream<Arguments> convexPolyTestData() {
        return Stream.of(
                new ShapeTestSetup(
                        ShapeTestSetup.ShapeInfo.builder().maxX(5).maxY(5).minX(1).minY(1).length(3).build(),
                        new Point(1, 1),
                        new Point(5, 1),
                        new Point(4, 5)),
                new ShapeTestSetup(
                        ShapeTestSetup.ShapeInfo.builder().maxX(679).maxY(954).minX(-143).minY(-788).length(5).build(),
                        new Point(679, 662),
                        new Point(589, 954),
                        new Point(492, 850),
                        new Point(-143, 86),
                        new Point(238, -788))
        ).map(Arguments::of);
    }

    private static Stream<Arguments> nonConvexPolyTestData() {
        return Stream.of(
                new ShapeTestSetup(
                        ShapeTestSetup.ShapeInfo.builder().length(3).build(),
                        new Point(-332, -426),
                        new Point(-845, -616),
                        new Point(865, 886),
                        new Point(347, -455)),
                new ShapeTestSetup(
                        ShapeTestSetup.ShapeInfo.builder().length(4).build(),
                        new Point(-470, 815),
                        new Point(-744, 940),
                        new Point(-744, 941),
                        new Point(865, 886),
                        new Point(563, -184),
                        new Point(-332, -426))
        ).map(Arguments::of);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("convexPolyTestData")
    void isConvexTrue(ShapeTestSetup testShape) {
        // ARRANGE
        shapeUnderTest.setCoordinates(testShape.getShapePoints());
        // ACT
        boolean isConvex = shapeUnderTest.isConvex();
        // ASSERT
        Assertions.assertTrue(isConvex);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nonConvexPolyTestData")
    void isConvexFalse(ShapeTestSetup testShape) {
        // ARRANGE
        shapeUnderTest.setCoordinates(testShape.getShapePoints());
        // ACT
        boolean isConvex = shapeUnderTest.isConvex();
        // ASSERT
        Assertions.assertFalse(isConvex);
    }

    @RepeatedTest(10)
    @DisplayName("Random shape with configuration")
    void generateRandomShapeIsValid() {
        // ARRANGE
        shapeUnderTest.generateRandomShape();
        System.out.println(shapeUnderTest.getCoordinates());
        // ACT
        boolean isConvex = shapeUnderTest.isConvex();
        // ASSERT
        Assertions.assertTrue(isConvex);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("nonConvexPolyTestData")
    void addValidPoints(ShapeTestSetup testShape) {
        // ARRANGE
        testShape.getShapePoints()
                .stream()
                .forEach(point -> shapeUnderTest.addPoint(point));
        // ACT
        int noOfCoords = shapeUnderTest.getCoordinates().size();
        // ASSERT
        Assertions.assertEquals(testShape.getShapeInfo().getLength(), noOfCoords);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("convexPolyTestData")
    void testMaxMinCoordinates(ShapeTestSetup testShape) {
        // ARRANGE
        testShape.getShapePoints()
                .stream()
                .forEach(point -> shapeUnderTest.addPoint(point));

        // ACT
        int noOfCoords = shapeUnderTest.getCoordinates().size();

        // ASSERT
        Assertions.assertEquals(testShape.getShapeInfo().getLength(), noOfCoords);
        Assertions.assertEquals(testShape.getShapeInfo().getMaxX(), shapeUnderTest.getMaxX());
        Assertions.assertEquals(testShape.getShapeInfo().getMinX(), shapeUnderTest.getMinX());
        Assertions.assertEquals(testShape.getShapeInfo().getMaxY(), shapeUnderTest.getMaxY());
        Assertions.assertEquals(testShape.getShapeInfo().getMinY(), shapeUnderTest.getMinY());
    }
}