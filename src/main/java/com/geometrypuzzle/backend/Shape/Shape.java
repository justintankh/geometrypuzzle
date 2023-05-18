package com.geometrypuzzle.backend.Shape;

import com.geometrypuzzle.backend.Point.Point;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Shape {
    /* shape is seen as a list of connected points in order */
    private List<Point> shape;

    public boolean isConvex() {
        Double orientation = null;
        double sumOfAngles = 0.0;
        /* This logic is built around the idea of convex polygon having
         * - the sum of its exterior angles equal to 360 degrees
         * - arc tangent having an orientation when traversing the line*/
        /* inspired by - https://www.youtube.com/watch?v=XOk0aGwZYn8 */

        if (!isPolygon()) return false;

        // Initializing the angle of the last two points as reference for the first point
        Point previousPoint = shape.get(shape.size() - 1);
        double previousThetha = atan2BetweenPoints(previousPoint, shape.get(shape.size() - 2));

        for (int i = 0; i < shape.size(); i++) {
            Point newPoint = shape.get(i);
            double newTheta = atan2BetweenPoints(newPoint, previousPoint);

            /* obtain normalized angle */
            double angle = normalizeAngle(newTheta - previousThetha);

            /* initialize orientation - if null*/
            orientation = Optional.ofNullable(orientation).orElse(angle > 0.0 ? 1.0 : -1.0);

            // If there is an orientation change ( concave ), or angle = 0.0 ( flat )
            boolean isConcave = orientation * angle <= 0.0;
            if (isConcave) return false;

            /* update previous values */
            sumOfAngles += angle;
            previousPoint = newPoint;
            previousThetha = newTheta;
        }
        return Math.abs(Math.round(sumOfAngles / (Math.PI * 2))) == 1;
    }

    private static double normalizeAngle(double angle) {
        if (angle <= -Math.PI) {
            angle += Math.PI * 2;

        } else if (angle > Math.PI) {
            angle -= Math.PI * 2;
        }
        return angle;
    }

    public boolean isPolygon() {
        return this.shape.size() > 2;
    }

    // TODO:
    // - Check for consecutive points, question on handling
    // ? If repeated, return False

    public static double atan2BetweenPoints(Point newPoint, Point oldPoint) {
        return Math.atan2(newPoint.getY() - oldPoint.getY(), newPoint.getX() - oldPoint.getX());
    }
}
