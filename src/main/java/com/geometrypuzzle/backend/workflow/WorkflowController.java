package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.session.StoreService;
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
    private StoreService storeService;
    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/startWorkflow")
    public Puzzle startWorkflow(@RequestBody String uuid) {
        boolean hasSession = storeService.hasSession(uuid);

        Shape storedShape = hasSession ? storeService.retrieveShape(uuid) : new Shape();
        Step storedStep = hasSession ? storeService.retrieveStep(uuid) : Step.START;

        if(!hasSession){
            /* Create new UUID */
            storeService.createStore(uuid);
        }

        // Construct factory workflow
        Workflow workflow = Workflow.builder()
                .processKey(uuid)
                .shape(storedShape)
                .step(storedStep)
                .build();

        WorkflowFactory factory = new WorkflowFactory(workflow);
        Puzzle puzzle = factory.triggerService();
        // Update next step - save to DB
        workflow.setStep(puzzle.getNextStep());
        storeService.updateStore(workflow);

        return puzzle;
    }

    @PostMapping("/sendMessage")
    public Puzzle sendMessage(@RequestBody RequestDetails request) {
        boolean hasSession = storeService.hasSession(request.getUuid());

        // Reconstruct Shape
        Shape retrievedShape = hasSession ? storeService.retrieveShape(request.getUuid()) : new Shape();

        // Construct factory workflow
        Workflow workflow = Workflow.builder()
                .step(request.getStep())
                .shape(retrievedShape)
                .build();

        WorkflowFactory factory = new WorkflowFactory(workflow);
        Puzzle puzzle = factory.triggerService();
        // Update next step - save to DB
        workflow.setStep(puzzle.getNextStep());
        storeService.updateStore(workflow);

        return puzzle;
    }

    @Data
    static
    class RequestDetails {
        String uuid;
        Step Step;
        Point point;
    }
}

