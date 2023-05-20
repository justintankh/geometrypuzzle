package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.shape.Shape;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@SpringBootApplication
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
    public Puzzle continueWorkflow(@RequestBody ContinueRequest request) {
        // Understand request and route to next step accordingly
        Workflow workflow = workflowService.processContinueWorkflow(request);

        // Run through workflow from message sent by frontend
        Puzzle response = workflowFactory.triggerService(workflow);

        // Update step for session persistence - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), response);

        /* Not filtered for debugging, to be removed */
        return response;
    }
    @Getter
    static class StartRequest {
        String processKey;
    }
    @Getter
    static class ContinueRequest {
        String processKey;
        Point point;
        String message;
    }

    @Builder
    @Getter
    static class FilteredResponse {
        Shape shape;
        Puzzle.PuzzleDisplay display;

        public static FilteredResponse adapter(Puzzle puzzle){
            return FilteredResponse.builder()
                    .shape(puzzle.getShape())
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

