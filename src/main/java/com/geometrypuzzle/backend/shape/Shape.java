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
    public void generateRandomShape() {
        int numberOfCoordinates = ShapeUtils.randomInt(RandomShape.VALID_MINIMUM, RandomShape.maxCoordinates);
        IntStream.range(0, numberOfCoordinates).forEach(
                val -> {
                    Point randomShape;
                    do {
                        int randomX = ShapeUtils.randomInt(RandomShape.minX, RandomShape.maxX);
                        int randomY = ShapeUtils.randomInt(RandomShape.minY, RandomShape.maxY);
                        randomShape = new Point(randomX, randomY);
                    } while (this.addPoint(randomShape));
                }
        );
    }

    public boolean addPoint(Point point) {
        /* Reject the point of it already exists */
        if(this.coordinates.contains(point)){
            return false;
        }

        /* Shape input validity is based on future shape */
        List<Point> newCoords = new ArrayList(this.coordinates);
        newCoords.add(point);

        /* Do not check for convex if shape still not formed */
        if (!ShapeUtils.isPolygon(newCoords)) {
            this.coordinates.add(point);
            /* If future is valid convex polygon */
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

    public boolean isPointInside(Point point){
        boolean inXBound = point.getX() >= this.minX && point.getX() <= this.maxX;
        boolean inYBound = point.getY() >= this.minY && point.getY() <= this.maxY;
        return inXBound && inYBound;
    }

    /* following is* wildcard, RestController will represent ( convex: boolean ) as response field
    *  - Even though * wildcard is not an attribute of class
    * */
    public boolean isConvex() {
        return ShapeUtils.isConvex(this.coordinates);
    }
}
