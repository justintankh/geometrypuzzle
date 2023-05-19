package com.geometrypuzzle.backend.puzzle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PuzzleDisplay {
    String displayBanner;
    String displayMessage;
    String displayInstructions;
}
