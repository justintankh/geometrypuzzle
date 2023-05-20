package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.ShapeConfig.RandomShape;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@Data
@NoArgsConstructor
public class Shape {

    private List<Point> coordinates = new ArrayList();
    private boolean polygon;
    private boolean convex;
    private Integer maxX;
    private Integer minX;
    private Integer maxY;
    private Integer minY;

    public void generateRandomShape() {
        this.coordinates = new ArrayList(); /* Remove this necessarily re-instantiation if we're not calling this method again */

        int numberOfCoordinates = ShapeUtils.randomInt(RandomShape.VALID_MINIMUM, RandomShape.maxCoordinates);
        Long timeStart = System.nanoTime();
        boolean isTwoLong;
        do {
            /* Thread safety */
            isTwoLong = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeStart) >= RandomShape.maxDurationTillFail;

            List<Point> points = new ArrayList();
            IntStream.range(0, numberOfCoordinates).forEach(val -> {
                int randomX = ShapeUtils.randomInt(RandomShape.minX, RandomShape.maxX);
                int randomY = ShapeUtils.randomInt(RandomShape.minY, RandomShape.maxY);
                points.add(new Point(randomX, randomY));
            });
            if (ShapeUtils.isConvex(points)) {
                points.forEach(point -> this.addPoint(point));
            }
        } while (this.coordinates.size() != numberOfCoordinates && !isTwoLong);
        log.info("Generating random shape took {} ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeStart));
        log.info("desired size {}, points {}", numberOfCoordinates, this.getCoordinates());
    }

    public boolean addPoint(Point point) {
        /* Reject the point of it already exists */
        if (this.coordinates.contains(point)) {
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

    public boolean isPointInside(Point point) {
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

    public boolean isPolygon() {
        return ShapeUtils.isPolygon(this.coordinates);
    }
}
