package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.shape.Shape;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@SpringBootApplication
@RestController
@RequestMapping("")
public class WorkflowController {


    private static boolean isHasSession(String uuid) {
        List<String> mockUUIDStore = List.of("mockId");
        return mockUUIDStore.contains(uuid);
    }
    private static Shape retrieveShape(String uuid) {
        // TODO: db
        Shape sessionShape = new Shape();
        sessionShape.generateRandomShape();
        return sessionShape;
    }

    private static STEP retrieveStep(String uuid) {
        // TODO: db
        return STEP.INCOMPLETE;
    }

    private static void createStore(String uuid){
        // TODO: db
    }

    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/startWorkflow")
    public Puzzle startWorkflow(@RequestBody String uuid) {
        boolean hasSession = isHasSession(uuid);

        Shape storedShape = hasSession ? retrieveShape(uuid) : new Shape();
        STEP storedStep = hasSession ? retrieveStep(uuid) : STEP.START;

        if(!hasSession){
            /* Create new UUID */
            createStore(uuid);
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
        boolean hasSession = isHasSession(request.getUuid());

        // Reconstruct Shape
        Shape retrievedShape = hasSession ? retrieveShape(request.getUuid()) : new Shape();

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
        STEP step;
        Point point;
    }
}

