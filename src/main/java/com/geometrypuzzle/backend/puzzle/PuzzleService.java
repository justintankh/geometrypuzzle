package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Workflow;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.MESSAGE;
import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.PLACEHOLDERS;
import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.REGEX;

@Slf4j
@AllArgsConstructor
public class PuzzleService {
    private Workflow.Step storedStep;
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

    public void addPoint() {
        boolean isAdded = shape.addPoint(point);

        Workflow.Step nextStep;
        if (!isAdded) {
            nextStep = Workflow.Step.INVALID;
        } else if (this.shape.isConvex()) {
            nextStep = Workflow.Step.COMPLETE;
        } else if (!this.shape.isPolygon()) {
            nextStep = Workflow.Step.INCOMPLETE;
        } else {
            log.error("Unknown condition slipped {}", this);
            throw new RuntimeException("Unknown condition slipped %s".formatted(this));
        }

        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(nextStep);

        this.puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(this.shape)
                .storeStep(nextStep)
                .storePoint(point)
                .build();
    }

    public void generateRandom() {
        // Generate random shape
        shape.generateRandomShape();
        
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Workflow.Step.RANDOM_SHAPE);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(shape)
                .storeStep(Workflow.Step.RANDOM_SHAPE)
                .build();
    }

    public void dispatchPage() {
        // Display based on stored step, for testing point logic is encapsulated in display
        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(getDisplay(storedStep))
                .shape(shape)
                .storePoint(point)
                .storeStep(storedStep)
                .build();
    }

    public Puzzle.PuzzleDisplay getDisplay(Workflow.Step step) {
        String nextIndex = String.valueOf(this.shape.getCoordinates().size());
        String enterNextCoordinates = MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex);

        return switch (step) {
            case ADD_POINT -> {
                /* Normal flow will not flow here */
                yield getDisplay(Workflow.Step.INCOMPLETE);
            }
            case START -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Welcome to the GIC geometry puzzle app")
                    .displayInstructions("[1] Create a custom shape\n" + "[2] Create a random shape")
                    .allowedRegex(List.of(REGEX.ONE_XOR_TWO))
                    .build();
            case INCOMPLETE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage(MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE)
                    .displayInstructions(enterNextCoordinates)
                    .allowedRegex(List.of(REGEX.COORDINATES))
                    .build();
            case RANDOM_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage("Your random shape is")
                    .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                    .build();
            case FINAL_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayMessage(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                    .displayInstructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
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
                        .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                        .build();
            }

            case INVALID -> {
                boolean isComplete = this.shape.isPolygon();
                String coordinates = "%s,%s".formatted(this.point.getX(), this.point.getY());
                String newCoordinatesIsInvalid = MESSAGE.INVALID_COORDINATES_BANNER.replace(PLACEHOLDERS.COORDINATES, coordinates);

                String yourCurrentShapeIs = MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE;
                String pleaseEnterSharpOrCoords = MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex);
                List<String> withSharpElseWithout = List.of(REGEX.COORDINATES);
                if (isComplete) {
                    yourCurrentShapeIs = MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE;
                    pleaseEnterSharpOrCoords = MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);
                    withSharpElseWithout = List.of(REGEX.COORDINATES, REGEX.SHARP);
                }


                yield Puzzle.PuzzleDisplay.builder()
                        .displayBanner(newCoordinatesIsInvalid)
                        .displayMessage(yourCurrentShapeIs)
                        .displayInstructions(pleaseEnterSharpOrCoords)
                        .allowedRegex(withSharpElseWithout)
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
                        .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                        .build();
            }
        };
    }
}
