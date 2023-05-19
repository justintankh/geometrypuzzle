package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.session.SessionService;
import com.geometrypuzzle.backend.shape.Shape;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@SpringBootApplication
@RestController
@RequestMapping("")
public class WorkflowController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/startWorkflow")
    public Puzzle startWorkflow(@RequestBody String uuid) {
        boolean hasSession = sessionService.hasSession(uuid);

        Shape storedShape = hasSession ? sessionService.retrieveShape(uuid) : new Shape();
        Step storedStep = hasSession ? sessionService.retrieveStep(uuid) : Step.START;

        if(!hasSession){
            /* Create new UUID */
            sessionService.createStore(uuid);
        }

        // Construct factory workflow
        Workflow workflow = Workflow.builder()
                .step(storedStep)
                .shape(storedShape)
                .build();

        WorkflowFactory factory = new WorkflowFactory(workflow);
        return factory.triggerService();
    }

    @PostMapping("/sendMessage")
    public Puzzle sendMessage(@RequestBody RequestDetails request) {
        boolean hasSession = sessionService.hasSession(request.getUuid());

        // Reconstruct Shape
        Shape retrievedShape = hasSession ? sessionService.retrieveShape(request.getUuid()) : new Shape();

        // Construct factory workflow
        Workflow workflow = Workflow.builder()
                .step(request.getStep())
                .shape(retrievedShape)
                .build();

        WorkflowFactory factory = new WorkflowFactory(workflow);
        return factory.triggerService();
    }

    @Data
    static
    class RequestDetails {
        String uuid;
        Step Step;
        Point point;
    }
}

