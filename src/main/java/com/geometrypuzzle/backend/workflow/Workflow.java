package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Data;

enum STEP {
    START,RANDOM, INCOMPLETE, COMPLETE, FINALIZED;
}

@Data
@Builder
public class Workflow {
    String uuid;
    STEP step;
    Shape shape;
}
