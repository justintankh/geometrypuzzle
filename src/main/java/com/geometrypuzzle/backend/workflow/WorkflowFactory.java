package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.puzzle.PuzzleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WorkflowFactory {
    private PuzzleService puzzleService;
    private STEP step;
    private Map<STEP, Puzzle> handler = new HashMap<>();

    public WorkflowFactory(Workflow workflow) {
        this.step = workflow.getStep();
        this.puzzleService = new PuzzleService(workflow.getShape());
        this.configureFactory();
    }

    @PostConstruct
    private Map<STEP, Puzzle> configureFactory() {
        log.info("Post Construct is called");
        handler.put(STEP.START, puzzleService.startPuzzle());
        handler.put(STEP.RANDOM, puzzleService.randomShapeGeneration());
        handler.put(STEP.INCOMPLETE, puzzleService.startPuzzle());
        handler.put(STEP.COMPLETE, puzzleService.startPuzzle());
        handler.put(STEP.FINALIZED, puzzleService.startPuzzle());
        return handler;
    }

    public Puzzle triggerService() {
        log.info("Calling step in Puzzle Service: {}", step);
        return handler.get(step);
    }
}
