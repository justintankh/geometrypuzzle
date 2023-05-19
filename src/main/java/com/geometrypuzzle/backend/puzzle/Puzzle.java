package com.geometrypuzzle.backend.puzzle;

import com.geometrypuzzle.backend.shape.Shape;

public class Puzzle {
    Shape currentShape;

    void StartWorkflow() {
        /* Check for matching UUID, continue workflow last leftOff */

        // Welcome to the GIC geometry puzzle app
        // - Please select one
        // [1] Generate a custom shape
        // [2] Generate a random shape
    }

    void DispatchPage() {
        // 1. Your current shape is incomplete
        // 2. Your {current}/{generated} shape is valid and complete
        // 3. Your finalized shape is ..
    }

    void RandomShapeGeneration() {
        // Your customer generated shape is
        // 1. (x, y)
        // 2. (x, y)
        // 3. (x, y)
        // 4. (x, y)
        // Please enter # to finalize your shape or coordinates (no) in x y format
    }
}
