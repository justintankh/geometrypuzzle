package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@SpringBootApplication
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("workflow")
public class WorkflowController {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WorkflowFactory workflowFactory;

    @PostMapping("start")
    public FilteredResponse startWorkflow(@RequestBody StartRequest request) {
        // Resumes from last session or else create new session
        Workflow workflow = workflowService.processStartWorkflow(request.getProcessKey());

        // Using stored data, run through workflow again
        Puzzle response = workflowFactory.triggerService(workflow);

        // Clean up response to send to frontend
        return FilteredResponse.adapter(response);
    }

    @PostMapping("restart")
    public FilteredResponse restartWorkflow(@RequestBody StartRequest request) {
        // Restart the session
        Workflow workflow = workflowService.processRestartWorkflow(request.getProcessKey());

        // Start workflow from search
        Puzzle response = workflowFactory.triggerService(workflow);

        // Don't need to update workflow as step was restarted, stored state is the same
        return FilteredResponse.adapter(response);
    }

    @PostMapping("continue")
    public FilteredResponse continueWorkflow(@RequestBody ContinueRequest request) {
        // Understand request and route to next step accordingly
        Workflow workflow = workflowService.processContinueWorkflow(request);

        // Run through workflow from message sent by frontend
        Puzzle response = workflowFactory.triggerService(workflow);

        // Update step for session persistence - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), response);

        /* Not filtered for debugging, to be changed */
        return FilteredResponse.adapter(response);
    }
    @Getter
    static class StartRequest {
        String processKey;
    }
    @Getter
    public static final class ContinueRequest {
        String processKey;
        Point point;
        MessageName message;
        @ToString
        public enum MessageName {
            /* Make sure enums values are retained when modifying, database infers as smallint */
            CUSTOM_SHAPE("CUSTOM_SHAPE"),
            RANDOM_SHAPE("RANDOM_SHAPE"),
            ADD_POINT("ADD_POINT"),
            TEST_POINT("TEST_POINT"),
            FINAL_SHAPE("FINAL_SHAPE"),
            QUIT("QUIT");

            MessageName(String value) {
            }
        }
    }

    @Builder
    @Getter
    static class FilteredResponse {
        Shape shape;
        Point point;
        Puzzle.PuzzleDisplay display;

        public static FilteredResponse adapter(Puzzle puzzle){
            return FilteredResponse.builder()
                    .shape(puzzle.getShape())
                    .point(puzzle.getStorePoint())
                    .display(puzzle.getPuzzleDisplay())
                    .build();
        }
    }

    /* for debugging, to be removed */
    @GetMapping
    public Object debugging() {
        return workflowService.getAllStore();
    }
}

