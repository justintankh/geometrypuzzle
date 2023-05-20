package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Workflow {
    String processKey;
    Step step;
    Shape shape;
    Point point;

    public enum Step {
        /* Make sure enums order are retained when adding Steps, database infers as smallint */
        START,
        ADD_POINT,
        INCOMPLETE,
        RANDOM_SHAPE,
        INVALID,
        COMPLETE,
        FINAL_SHAPE,
        TEST_POINT,
        QUIT
    }
}
