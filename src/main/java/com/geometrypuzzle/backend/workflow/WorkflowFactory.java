package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.puzzle.PuzzleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
public class WorkflowFactory {
    private Map<Step, Consumer<PuzzleService>> handler = new HashMap<>();

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

    public Puzzle triggerService(Workflow workflow) {
        PuzzleService puzzleService = new PuzzleService(workflow.getShape());
        log.info("Calling step in Puzzle Service: {}", workflow.getStep());
        /* Obtaining the method corresponding to the STEP */
        Consumer<PuzzleService> serviceConsumer = handler.get(workflow.getStep());
        /* Calls the method in the service */
        serviceConsumer.accept(puzzleService);
        /* Return the mutated Puzzle class */
        return puzzleService.getPuzzleDetails();
    }
}
