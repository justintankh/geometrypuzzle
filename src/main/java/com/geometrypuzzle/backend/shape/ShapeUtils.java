package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;

public class ShapeUtils {
    private ShapeUtils() {
        /* Sonarlint compliance */
        throw new IllegalStateException("Utility class");
    }

    // TODO:
    // - Check for consecutive points, question on handling
    // ? If repeated, return False
    static double normalizeAngle(double angle) {
        if (angle <= -Math.PI) {
            angle += Math.PI * 2;

        } else if (angle > Math.PI) {
            angle -= Math.PI * 2;
        }
        return angle;
    }

    static double atan2BetweenPoints(Point newPoint, Point oldPoint) {
        return Math.atan2(newPoint.getY() - oldPoint.getY(), newPoint.getX() - oldPoint.getX());
    }
}
