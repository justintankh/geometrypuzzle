package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
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
    public Puzzle startWorkflow(@RequestBody StartRequest request) {
        // Resumes from last session or else create new session
        Workflow workflow = workflowService.processStartWorkflow(request.getProcessKey());

        // Construct factory workflow
        Puzzle response = workflowFactory.triggerService(workflow);

        // Update next step - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), response);

        return response;
    }

    @PostMapping("restart")
    public Puzzle restartWorkflow(@RequestBody StartRequest request) {
        // Restart the session
        Workflow workflow = workflowService.processRestartWorkflow(request.getProcessKey());

        // Construct factory workflow
        Puzzle puzzle = workflowFactory.triggerService(workflow);

        // Update step for session persistence - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), puzzle);

        return puzzle;
    }

    @PostMapping("continue")
    public Puzzle continueWorkflow(@RequestBody MessageRequest request) {
        // Understand request and route to next step accordingly
        Workflow workflow = workflowService.processContinueWorkflow(request);

        // Construct factory workflow
        Puzzle puzzle = workflowFactory.triggerService(workflow);

        // Update step for session persistence - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), puzzle);

        return puzzle;
    }
    @Getter
    static class StartRequest {
        String processKey;
    }
    @Getter
    static class MessageRequest {
        String processKey;
        Point point;
        String message;
    }

    /* for debugging, to be removed */
    @GetMapping
    public Object debugging() {
        return workflowService.getAllStore();
    }
}

