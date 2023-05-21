package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.workflow.Workflow.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Puzzle {
    // To update states in store
    Step storeStep;
    Point storePoint;
    Shape shape;
    // For response to frontend
    PuzzleDisplay puzzleDisplay;

    @Data
    @Builder
    public static class PuzzleDisplay {
        String displayBanner;
        String displayMessage;
        String displayInstructions;
        List<String> allowedRegex;

        public static final class CONST {

            public static final class PLACEHOLDERS {
                public static final String INDEX = "{index}";
                public static final String COORDINATES = "{x,y}";
            }

            public static final class MESSAGE {
                public static final String ENTER_COORDINATES_MESSAGE = "Please enter coordinates {index} in x y format";
                public static final String INVALID_COORDINATES_BANNER = ("New coordinates ({x,y}) is invalid!!!\n" + "Not adding new coordinates to the current shape.");
                public static final String YOUR_CURRENT_SHAPE_IS_INCOMPLETE = "Your current shape is incomplete";
                public static final String YOUR_CURRENT_SHAPE_IS_VALID_AND_COMPLETE = "Your current shape is valid and complete";
                public static final String FINALIZED_SHAPE_OR_NEXT_COORDINATES_INSTRUCTIONS = "Please enter # to finalize your shape or enter coordinates {index} in x y format";
                public static final String WITHIN_FINALIZED_SHAPE = "Coordinates ({x,y}) is within your finalized shape";
                public static final String OUTSIDE_FINALIZED_SHAPE = "Sorry, coordinates ({x,y}) is outside of your finalized shape";
                public static final String FINALIZED_DISPLAY_INSTRUCTIONS = "Please key in test coordinates in x y format or enter # to quit the game";
                public static final String FINALIZED_SHAPE_MESSAGE = "Your finalized shape is";
            }

            public static final class REGEX {
                public static final String ONE_XOR_TWO = "^[12]$";
                public static final String COORDINATES = "^\\d+\\s\\d+";
                public static final String SHARP = "#";
            }

        }
    }
}
