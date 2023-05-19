package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Data;


enum STEP {
    START, INCOMPLETE, COMPLETE, FINALIZED;
}

@Data
@Builder
public class Workflow {
    String uuid;
    STEP step;
    Shape shape;
}
