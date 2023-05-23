package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.shape.ShapeConfig;
import com.geometrypuzzle.backend.workflow.Workflow;
import com.geometrypuzzle.backend.workflow.WorkflowController.ContinueRequest.MessageName;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.geometrypuzzle.backend.puzzle.Puzzle.PuzzleDisplay.CONST.*;

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
        if (shape.getCoordinates().size() == 0 || ShapeConfig.RandomShape.ALLOW_REGENERATE) {
            shape.generateRandomShape();
        }

        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Workflow.Step.RANDOM_SHAPE);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(shape)
                .storeStep(Workflow.Step.RANDOM_SHAPE)
                .build();
    }

    public void quitWorkflow() {

        // Clear the shape that is stored
        Puzzle.PuzzleDisplay puzzleDisplay = getDisplay(Workflow.Step.QUIT);

        puzzleDetails = Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(new Shape())
                .storeStep(Workflow.Step.QUIT)
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
                    .banner(null)
                    .message("Welcome to the GIC geometry puzzle app")
                    .instructions("[1] Create a custom shape\n" + "[2] Create a random shape")
                    .allowedRegex(List.of(REGEX.ONE_XOR_TWO))
                    .allowedFlows(List.of(MessageName.CUSTOM_SHAPE, MessageName.RANDOM_SHAPE))
                    .build();
            case INCOMPLETE -> Puzzle.PuzzleDisplay.builder()
                    .banner(null)
                    .message(MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE)
                    .instructions(enterNextCoordinates)
                    .allowedRegex(List.of(REGEX.COORDINATES))
                    .allowedFlows(List.of(MessageName.ADD_POINT))
                    .build();
            case RANDOM_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .banner(null)
                    .message("Your random shape is")
                    .instructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                    .allowedFlows(List.of(MessageName.TEST_POINT, MessageName.QUIT))
                    .build();
            case FINAL_SHAPE -> Puzzle.PuzzleDisplay.builder()
                    .banner(null)
                    .message(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                    .instructions(MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS)
                    .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                    .allowedFlows(List.of(MessageName.TEST_POINT, MessageName.QUIT))
                    .build();
            case QUIT -> Puzzle.PuzzleDisplay.builder()
                    .banner(null)
                    .message("Thank you for playing the GIC geometry puzzle app\n" + "Have a nice day!")
                    .instructions(null)
                    .allowedFlows(null)
                    .build();

            case COMPLETE -> {
                String finalizeShapeOrAddCoordinates = MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);
                yield Puzzle.PuzzleDisplay.builder()
                        .banner(null)
                        .message(MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE)
                        .instructions(finalizeShapeOrAddCoordinates)
                        .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                        .allowedFlows(List.of(MessageName.ADD_POINT, MessageName.FINAL_SHAPE))
                        .build();
            }

            case INVALID -> {
                boolean isComplete = this.shape.isPolygon();
                String coordinates = "%s,%s".formatted(this.point.getX(), this.point.getY());
                String newCoordinatesIsInvalid = MESSAGE.INVALID_COORDINATES_BANNER.replace(PLACEHOLDERS.COORDINATES, coordinates);

                String yourCurrentShapeIs = MESSAGE.YOUR_CURRENT_SHAPE_IS_INCOMPLETE;
                String pleaseEnterSharpOrCoords = MESSAGE.ENTER_COORDINATES_MESSAGE.replace(PLACEHOLDERS.INDEX, nextIndex);
                List<String> withSharpElseWithout = List.of(REGEX.COORDINATES);
                List<MessageName> allowedFlows = List.of(MessageName.ADD_POINT);
                if (isComplete) {
                    yourCurrentShapeIs = MESSAGE.YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE;
                    pleaseEnterSharpOrCoords = MESSAGE.FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS.replace(PLACEHOLDERS.INDEX, nextIndex);
                    withSharpElseWithout = List.of(REGEX.COORDINATES, REGEX.SHARP);
                    allowedFlows = List.of(MessageName.ADD_POINT, MessageName.FINAL_SHAPE);
                }

                yield Puzzle.PuzzleDisplay.builder()
                        .banner(newCoordinatesIsInvalid)
                        .message(yourCurrentShapeIs)
                        .instructions(pleaseEnterSharpOrCoords)
                        .allowedRegex(withSharpElseWithout)
                        .allowedFlows(allowedFlows)
                        .build();
            }

            case TEST_POINT -> {
                boolean isInsideShape = this.shape.isPointInside(this.point);
                String resultMessage = isInsideShape ? MESSAGE.WITHIN_FINALIZED_SHAPE : MESSAGE.OUTSIDE_FINALIZED_SHAPE;

                String coordinates = "%s,%s".formatted(this.point.getX(), this.point.getY());
                String finalizedResultsKeyInTest = resultMessage.replace(PLACEHOLDERS.COORDINATES, coordinates) + "\n" + MESSAGE.FINALIZED_DISPLAY_INSTRUCTIONS;

                yield Puzzle.PuzzleDisplay.builder()
                        .banner(null)
                        .message(MESSAGE.FINALIZED_SHAPE_MESSAGE)
                        .instructions(finalizedResultsKeyInTest)
                        .allowedRegex(List.of(REGEX.COORDINATES, REGEX.SHARP))
                        .allowedFlows(List.of(MessageName.TEST_POINT, MessageName.QUIT))
                        .build();
            }
        };
    }
}
