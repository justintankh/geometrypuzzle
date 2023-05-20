package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Step;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PuzzleService {
    private String message;
    private Shape storedShape;
    private Puzzle puzzleDetails;

    public PuzzleService(Shape shape) {
        this.storedShape = shape;
    }
    public PuzzleService(Shape shape, String message) {
        this.storedShape = shape;
    }

    public Puzzle getPuzzleDetails() {
        return this.puzzleDetails;
    }

    public void startPuzzle() {
        /* Check for matching UUID, continue workflow last leftOff */

        // Welcome to the GIC geometry puzzle app
        // - Please select one
        // [1] Generate a custom shape
        // [2] Generate a random shape
        Puzzle.PuzzleDisplay puzzleDisplay = Puzzle.PuzzleDisplay.builder()
                .displayBanner("Welcome to the GIC geometry puzzle app")
                .displayMessage("- Please select one")
                .displayInstructions("[1] Create a custom shape\n" + "[2] Create a random shape")
                .build();

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.INCOMPLETE)
                .build();
    }

    public void dispatchPage() {
        // 1. Your current shape is incomplete
        // 2. Your {current}/{generated} shape is valid and complete
        // 3. Your finalized shape is ..
    }

    public void randomShapeGeneration() {
        storedShape.generateRandomShape();

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

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.COMPLETE)
                .build();
    }
}
