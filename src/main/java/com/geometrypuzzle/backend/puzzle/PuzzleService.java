package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PuzzleService {

    Shape shape;

    public Puzzle startPuzzle() {
        /* Check for matching UUID, continue workflow last leftOff */

        // Welcome to the GIC geometry puzzle app
        // - Please select one
        // [1] Generate a custom shape
        // [2] Generate a random shape
        return null;
    }

    public Puzzle dispatchPage() {
        // 1. Your current shape is incomplete
        // 2. Your {current}/{generated} shape is valid and complete
        // 3. Your finalized shape is ..
        return null;
    }

    public Puzzle randomShapeGeneration() {
        shape.generateRandomShape();

        // Your customer generated shape is
        // 1. (x, y)
        // 2. (x, y)
        // 3. (x, y)
        // 4. (x, y)
        // Please enter # to finalize your shape or coordinates (no) in x y format
        Puzzle.PuzzleDisplay puzzleDisplay = Puzzle.PuzzleDisplay.builder()
                .displayBanner("Your customer generated shape is")
                .displayMessage("mockMessage")
                .displayInstructions("Please enter # to finalize your shape or coordinates (no) in x y format")
                .build();

        return Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(shape)
                .build();
    }
}
