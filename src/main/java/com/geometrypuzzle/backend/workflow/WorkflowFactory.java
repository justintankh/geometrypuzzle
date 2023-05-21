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
    private Map<Workflow.Step, Consumer<PuzzleService>> handler = new HashMap<>();

    @PostConstruct
    private Map<Workflow.Step, Consumer<PuzzleService>> configureFactory() {
        log.info("Post Construct is called");
        /* Steps with logic requiring isolated handling*/
        handler.put(Workflow.Step.RANDOM_SHAPE, PuzzleService::generateRandom);
        handler.put(Workflow.Step.ADD_POINT, PuzzleService::addPoint);
        handler.put(Workflow.Step.QUIT, PuzzleService::quitWorkflow);
        /* Steps with generic logic, Require dispatch */
        handler.put(Workflow.Step.START, PuzzleService::dispatchPage);
        handler.put(Workflow.Step.TEST_POINT, PuzzleService::dispatchPage);
        handler.put(Workflow.Step.INVALID, PuzzleService::dispatchPage);
        handler.put(Workflow.Step.INCOMPLETE, PuzzleService::dispatchPage);
        handler.put(Workflow.Step.COMPLETE, PuzzleService::dispatchPage);
        handler.put(Workflow.Step.FINAL_SHAPE, PuzzleService::dispatchPage);
        return handler;
    }

    public Puzzle triggerService(Workflow workflow) {
        log.info("Calling step in Puzzle Service: {}", workflow.getStep());
        PuzzleService puzzleService = new PuzzleService(workflow);
        /* Obtaining the method corresponding to the STEP */
        Consumer<PuzzleService> serviceConsumer = handler.get(workflow.getStep());
        /* Calls the method in the service */
        serviceConsumer.accept(puzzleService);
        /* Return the mutated Puzzle class */
        return puzzleService.getPuzzleDetails();
    }
}
