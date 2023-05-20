package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Puzzle {
    PuzzleDisplay puzzleDisplay;
    Shape shape;
    Step nextStep;

    @Data
    @Builder
    public static class PuzzleDisplay {
        String displayBanner;
        String displayMessage;
        String displayInstructions;
    }
}
