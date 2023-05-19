package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.puzzle.PuzzleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class WorkflowFactory {
    private Step step;
    private PuzzleService puzzleService;
    private Map<Step, Consumer<PuzzleService>> handler = new HashMap<>();

    public WorkflowFactory(Workflow workflow) {
        this.step = workflow.getStep();
        this.puzzleService = new PuzzleService(workflow.getShape());
        this.configureFactory();
    }

    // TODO: Tie up beans
    @PostConstruct
    private Map<Step, Consumer<PuzzleService>> configureFactory() {
        log.info("Post Construct is called");
        /* Handling as a consumer because we don't want to prematurely call the service */
        handler.put(Step.START, service -> service.startPuzzle());
        handler.put(Step.RANDOM, service -> service.randomShapeGeneration());
        handler.put(Step.INCOMPLETE, service -> service.startPuzzle());
        handler.put(Step.COMPLETE, service -> service.startPuzzle());
        /* Sonarlint compliant - not replacing the rest for readability */
        handler.put(Step.FINALIZED, PuzzleService::startPuzzle);
        return handler;
    }

    public Puzzle triggerService() {
        log.info("Calling step in Puzzle Service: {}", step);
        /* Obtaining the method corresponding to the STEP */
        Consumer<PuzzleService> serviceConsumer = handler.get(step);
        /* Calls the method in the service */
        serviceConsumer.accept(puzzleService);

        /* Obtain the mutated Puzzle class */
        return this.puzzleService.getPuzzleResponse();
    }
}
