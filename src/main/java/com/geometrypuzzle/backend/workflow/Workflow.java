package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Workflow {
    String processKey;
    String businessKey;
    Step step;
    Shape shape;
}
