package com.geometrypuzzle.backend.shape;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Data
public class ShapeConfig {
    private ShapeConfig() {
        throw new IllegalStateException("Utility class"); /* Sonarlint compliance */
    }

    public static final class RandomShape {
        private RandomShape() {
            throw new IllegalStateException("Utility class"); /* Sonarlint compliance */
        }
        // TODO: Connect springboot @Value
        public static final boolean ALLOW_REGENERATE = false;
        public static final int VALID_MINIMUM = 3;
        public static final long maxDurationTillFail = TimeUnit.MILLISECONDS.toMillis(1000);
        @Value("${randomShapeConfig.maxCoordinates:10}")
        public static int maxCoordinates = 8;
        @Value("${randomShapeConfig.maximum.x}")
        public static int maxX = 20;
        @Value("${randomShapeConfig.maximum.x}")
        public static int maxY = 20;
        @Value("${randomShapeConfig.minimum.x}")
        public static int minX = -20;
        @Value("${randomShapeConfig.minimum.y}")
        public static int minY = -20;
    }
}
