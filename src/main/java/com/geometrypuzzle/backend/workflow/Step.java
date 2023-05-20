package com.geometrypuzzle.backend.workflow;

public enum Step {
    /* Make sure enums order are retained when adding Steps, database infers as smallint */
    START,
    INCOMPLETE,
    RANDOM,
    INVALID,
    COMPLETE,
    FINALIZED,
    TEST
}
