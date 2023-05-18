package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;

import java.util.List;
import java.util.Optional;

public class ShapeUtils {
    private ShapeUtils() {
        throw new IllegalStateException("Utility class"); /* Sonarlint compliance */
    }

    static boolean isPolygon(List<Point> coordinates) {
        return coordinates.size() > 2;
    }

    static boolean isConvex(List<Point> coordinates) {
        /* This logic is built around the concept of convex polygon having
         * - the sum of its exterior angles equal to 360 degrees
         * - arc tangent having one fixed orientation while traversing its lines,
         *  e.g. as initiated CW or A-CW */
        if (!isPolygon(coordinates)) return false;

        Double orientation = null;
        double sumOfAngles = 0.0;
        // Initializing the angle of the last two points as reference for the first point
        Point previousPoint = coordinates.get(coordinates.size() - 1);
        double previousThetha = ShapeUtils.atan2BetweenPoints(previousPoint, coordinates.get(coordinates.size() - 2));

        for (int i = 0; i < coordinates.size(); i++) {
            Point newPoint = coordinates.get(i);
            double newTheta = ShapeUtils.atan2BetweenPoints(newPoint, previousPoint);

            /* obtain normalized angle */
            double angle = ShapeUtils.normalizeAngle(newTheta - previousThetha);

            /* initialize orientation - if null*/
            orientation = Optional.ofNullable(orientation).orElse(angle > 0.0 ? 1.0 : -1.0);

            // If there is an orientation change ( concave ), or angle = 0.0 ( flat, handles for scenario of consecutive points )
            boolean isOrientationChange = orientation * angle < 0.0;
            boolean isFlat = angle == 0.0;
            if (isOrientationChange || isFlat) {
                return false;
            }

            /* update previous values */
            sumOfAngles += angle;
            previousPoint = newPoint;
            previousThetha = newTheta;
        }
        return Math.abs(Math.round(sumOfAngles / (Math.PI * 2))) == 1;
    }

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

    static int randomInt(int min, int max) {
        return new java.util.SplittableRandom().nextInt(min, max);
    }
}
