package com.geometrypuzzle.backend.shape;

import com.geometrypuzzle.backend.point.Point;
import lombok.Getter;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

@Getter
public class ShapeTestSetup {
        private final List<Point> shapePoints;
        public ShapeTestSetup(Point... shapePoints){
            this.shapePoints = List.of(shapePoints);
        }
        public Arguments getTestArgs(){
            return Arguments.of(this);
        }
        @Override
        public String toString(){
            return "%s coordinates".formatted(this.shapePoints.size());
        }
}
