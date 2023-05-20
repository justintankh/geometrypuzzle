package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ShapeTestSetup {
    @Builder
    @Getter
    public static class ShapeInfo {
        private int maxX, maxY, minX, minY, length;
    }
    private final List<Point> shapePoints;
    private final ShapeInfo shapeInfo;
    ShapeTestSetup(ShapeInfo shapeInfo, Point... shapePoints) {
        this.shapeInfo = shapeInfo;
        this.shapePoints = List.of(shapePoints);
    }

    @Override
    public String toString() {
        return "%s points, length: %s".formatted(this.shapePoints.size(), this.shapeInfo.getLength());
    }
}
