package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Step;
import lombok.AllArgsConstructor;

import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.*;

@AllArgsConstructor
public class PuzzleService {
    private Point pointToAdd;
    private Shape storedShape;
    private Puzzle puzzleDetails;

    public PuzzleService(Shape shape, Point point) {
        this.storedShape = shape;
        this.pointToAdd = point;
    }

    public Puzzle getPuzzleDetails() {
        return this.puzzleDetails;
    }

    public void startPuzzle() {
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.START);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.INCOMPLETE)
                .build();
    }

    public void customShapeGeneration() {
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.START);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.INCOMPLETE)
                .build();
    }

    public void randomShapeGeneration() {
        storedShape.generateRandomShape();

        // User will see FINALIZED display for if session persisted
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.RANDOM);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.FINALIZED)
                .build();
    }

    public void finalizedPage() {
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.FINALIZED);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(storedShape)
                .nextStep(Step.COMPLETE)
                .build();
    }

    public Puzzle.PuzzleDisplay getDisplay(Step step) {
        String nextIndex = String.valueOf(this.storedShape.getCoordinates().size());
        String enterNextCoordinates = MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex);

        return switch(step) {
            case START -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Welcome to the GIC geometry puzzle app")
                    .displayInstructions("[1] Create a custom shape\n" + "[2] Create a random shape")
                    .build();
            case INCOMPLETE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage(MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE)
                    .displayInstructions(enterNextCoordinates)
                    .build();
            case RANDOM -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Your random shape is")
                    .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .build();
            case INVALID -> {
                /* Enclosing inside here as Points might be passed in as Null. - Limiting usage to flow to reduce null exceptions */
                String coordinates = "%s,%s".formatted(this.pointToAdd.getX(), this.pointToAdd.getY());
                String newCoordinatesIsInvalid = MESSAGE.INVALID_COORDINATES_BANNER.replace(PLACEHOLDERS.COORDINATES, coordinates);
                String currentShapeIsValid = this.storedShape.isPolygon() ? MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE : MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE;

                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(newCoordinatesIsInvalid)
                        .displayMessage(currentShapeIsValid)
                        .displayInstructions(enterNextCoordinates)
                        .build();
            }
            case COMPLETE -> {
                String finalizeShapeOrAddCoordinates = MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);

                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(null)
                        .displayMessage(MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE)
                        .displayInstructions(finalizeShapeOrAddCoordinates)
                        .build();
            }
            case FINALIZED -> {
                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(null)
                        .displayMessage(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                        .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                        .build();
            }
            case TEST -> {
                /* Enclosing inside here as Points might be passed in as Null. - Limiting usage to flow to reduce null exceptions */
                boolean isInsideShape = this.storedShape.isPointInside(this.pointToAdd);
                String resultMessage = isInsideShape ? MESSAGE.WITHIN_FINALIZED_SHAPE : MESSAGE.OUTSIDE_FINALIZED_SHAPE;

                String coordinates = "%s,%s".formatted(this.pointToAdd.getX(), this.pointToAdd.getY());
                String finalizedResultsKeyInTest = resultMessage.replace(PLACEHOLDERS.COORDINATES, coordinates) + "\n" + MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS;

                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(null)
                        .displayMessage(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                        .displayInstructions(finalizedResultsKeyInTest)
                        .build();
            }
        };
        // 1. Your current shape is incomplete
        // 2. Your {current}/{generated} shape is valid and complete
        // 3. Your finalized shape is ..
    }
}
