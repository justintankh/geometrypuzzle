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

    @PostMapping("start")
    public Puzzle startWorkflow(@RequestBody StartRequest request) {
        Workflow workflow = workflowService.restartWorkflow(request.getUuid());

        // Construct factory workflow
        WorkflowFactory factory = new WorkflowFactory(workflow);
        Puzzle puzzle = factory.triggerService();

        // Update next step - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), puzzle);

        return puzzle;
    }

    @PostMapping("continue")
    public Puzzle continueWorkflow(@RequestBody MessageRequest request) {
        Workflow workflow = workflowService.retrieveWorkflow(request.getUuid());

        // Construct factory workflow
        WorkflowFactory factory = new WorkflowFactory(workflow);
        Puzzle puzzle = factory.triggerService();

        // Update next step - save to DB
        workflowService.updateWorkflow(workflow.getProcessKey(), puzzle);

        return puzzle;
    }
    @Getter
    static class StartRequest {
        String uuid;
    }
    @Getter
    static class MessageRequest {
        String uuid;
        Point point;
    }

    /* for debugging, to be removed */
    @GetMapping
    public Object debugging() {
        return workflowService.getAllStore();
    }
}

