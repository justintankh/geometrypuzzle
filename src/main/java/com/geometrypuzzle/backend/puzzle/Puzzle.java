package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Puzzle {
    Shape shape;
    PuzzleDisplay puzzleDisplay;

    @Data
    @Builder
    public static class PuzzleDisplay {
        String displayBanner;
        String displayMessage;
        String displayInstructions;
    }
}
