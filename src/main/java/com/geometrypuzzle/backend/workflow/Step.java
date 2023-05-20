package com.geometrypuzzle.backend.workflow;

public enum Step {
    /* Make sure enums order are retained when adding Steps, database infers as smallint */
    START,
    ADD_POINT,
    INCOMPLETE,
    RANDOM_SHAPE,
    INVALID,
    COMPLETE,
    FINAL_SHAPE,
    TEST_POINT,
    QUIT
}
