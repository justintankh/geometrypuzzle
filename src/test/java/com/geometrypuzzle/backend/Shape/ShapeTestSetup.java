package com.geometrypuzzle.backend.Shape;

import com.geometrypuzzle.backend.Point.Point;
import lombok.Getter;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShapeTestSetup {
        private List<Point> shapePoints;
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
