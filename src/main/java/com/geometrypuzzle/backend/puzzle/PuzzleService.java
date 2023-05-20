package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Step;
import com.geometrypuzzle.backend.workflow.Workflow;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.MESSAGE;
import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.PLACEHOLDERS;

@Slf4j
@AllArgsConstructor
public class PuzzleService {
    private Step storedStep;
    private Shape shape;
    private Point point;
    private Puzzle puzzleDetails;

    public PuzzleService(Workflow workflow) {
        this.storedStep = workflow.getStep();
        this.shape = workflow.getShape();
        this.point = workflow.getPoint();
    }

    public Puzzle getPuzzleDetails() {
        return this.puzzleDetails;
    }

    public void startPuzzle() {
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.START);

        this.puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(this.shape)
                .storeStep(Step.START)
                .build();
    }

    public void addPoint() {
        boolean isAdded = shape.addPoint(point);

        Step nextStep;
        if (!isAdded) {
            nextStep = Step.INVALID;
        } else if (this.shape.isConvex()) {
            nextStep = Step.COMPLETE;
        } else if (!this.shape.isPolygon()) {
            nextStep = Step.INCOMPLETE;
        } else {
            log.error("Unknown condition slipped {}", this);
            throw new RuntimeException("Unknown condition slipped %s".formatted(this));
        }

        this.puzzleDetails = Puzzle.builder()
                .puzzleDisplay(getDisplay(nextStep))
                .shape(this.shape)
                .storeStep(nextStep)
                .build();
    }

    public void generateRandom() {
        // Generate random shape
        shape.generateRandomShape();

        // User will see FINALIZED display for if session persisted
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Step.RANDOM_SHAPE);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(shape)
                .storeStep(Step.FINAL_SHAPE)
                .build();
    }

    public void dispatchPage() {
        // Display based on stored step, for testing point logic is encapsulated in display
        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(getDisplay(storedStep))
                .shape(shape)
                .storeStep(storedStep)
                .build();
    }

    public Puzzle.PuzzleDisplay getDisplay(Step step) {
        String nextIndex = String.valueOf(this.shape.getCoordinates().size());
        String enterNextCoordinates = MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex);

        return switch (step) {
            case ADD_POINT -> {
                yield getDisplay(Step.INVALID);
            }
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
            case RANDOM_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Your random shape is")
                    .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .build();
            case FINAL_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                    .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .build();
            case QUIT -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Thank you for playing the GIC geometry puzzle app\n" + "Have a nice day!")
                    .displayInstructions(null)
                    .build();

            case COMPLETE -> {
                String finalizeShapeOrAddCoordinates = MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);
                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(null)
                        .displayMessage(MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE)
                        .displayInstructions(finalizeShapeOrAddCoordinates)
                        .build();
            }

            case INVALID -> {
                boolean isComplete = this.shape.isPolygon();
                String coordinates = "%s,%s".formatted(this.point.getX(), this.point.getY());
                String newCoordinatesIsInvalid = MESSAGE.INVALID_COORDINATES_BANNER.replace(PLACEHOLDERS.COORDINATES, coordinates);
                String yourCurrentShapeIs = isComplete ?
                        MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE :
                        MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE;
                String pleaseEnterSharpOrCoords = isComplete ?
                        MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex) :
                        MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);

                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(newCoordinatesIsInvalid)
                        .displayMessage(yourCurrentShapeIs)
                        .displayInstructions(pleaseEnterSharpOrCoords)
                        .build();
            }

            case TEST_POINT -> {
                boolean isInsideShape = this.shape.isPointInside(this.point);
                String resultMessage = isInsideShape ? MESSAGE.WITHIN_FINALIZED_SHAPE : MESSAGE.OUTSIDE_FINALIZED_SHAPE;

                String coordinates = "%s,%s".formatted(this.point.getX(), this.point.getY());
                String finalizedResultsKeyInTest = resultMessage.replace(PLACEHOLDERS.COORDINATES, coordinates) + "\n" + MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS;

                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(null)
                        .displayMessage(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                        .displayInstructions(finalizedResultsKeyInTest)
                        .build();
            }
        };
    }
}
