package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.ShapeConfig.RandomShape;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class Shape {

    private List<Point> coordinates = new ArrayList();
    private Integer maxX;
    private Integer minX;
    private Integer maxY;
    private Integer minY;

    public boolean addPoint(Point point) {
        /* Shape input validity is based on future shape */
        List<Point> newCoords = new ArrayList(this.coordinates);
        newCoords.add(point);

        /* Do not check for convex if shape still not formed */
        if (!ShapeUtils.isPolygon(newCoords)) {
            this.coordinates.add(point);
        } else if (ShapeUtils.isConvex(newCoords)) {
            this.coordinates.add(point);
        } else {
            /* rejected point */
            return false;
        }
        /* Update new zone */
        boolean uninitialized = maxX == null;
        if (uninitialized || maxX < point.getX()) maxX = point.getX();
        if (uninitialized || maxY < point.getY()) maxY = point.getY();
        if (uninitialized || minX > point.getX()) minX = point.getX();
        if (uninitialized || minY > point.getY()) minY = point.getY();
        return true;
    }

    private boolean isPolygon() {
        return ShapeUtils.isPolygon(this.coordinates);
    }

    public boolean isConvex() {
        return ShapeUtils.isConvex(this.coordinates);
    }

    public void generateRandomShape() {
        int numberOfCoordinates = ShapeUtils.randomInt(RandomShape.VALID_MINIMUM, RandomShape.maxCoordinates);
        IntStream.range(0, numberOfCoordinates).forEach(
                val -> {
                    int randomX, randomY;
                    Point randomShape;
                    do {
                        randomX = ShapeUtils.randomInt(RandomShape.minX, RandomShape.maxX);
                        randomY = ShapeUtils.randomInt(RandomShape.minY, RandomShape.maxY);
                        randomShape = new Point(randomX, randomY);
                    } while (this.addPoint(randomShape));
                }
        );
    }
}
