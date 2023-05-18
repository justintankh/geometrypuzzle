package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class Shape {
    /* shape is seen as a list of connected points in order */
    private List<Point> coordinates;

    public boolean isConvex() {
        Double orientation = null;
        double sumOfAngles = 0.0;
        /* This logic is built around the idea of convex polygon having
         * - the sum of its exterior angles equal to 360 degrees
         * - arc tangent having an orientation when traversing the line*/
        /* inspired by - https://www.youtube.com/watch?v=XOk0aGwZYn8 */

        if (!isPolygon()) return false;

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
            boolean isConcave = orientation * angle <= 0.0;
            if (isConcave) return false;

            /* update previous values */
            sumOfAngles += angle;
            previousPoint = newPoint;
            previousThetha = newTheta;
        }
        return Math.abs(Math.round(sumOfAngles / (Math.PI * 2))) == 1;
    }

    private boolean isPolygon() {
        return this.coordinates.size() > 2;
    }
}
