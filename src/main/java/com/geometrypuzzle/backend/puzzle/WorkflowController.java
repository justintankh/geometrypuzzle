package com.geometrypuzzle.backend.puzzle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("")
public class WorkflowController {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/startWorkflow")
    public Workflow startWorkflow(@RequestBody String uuid) throws JsonProcessingException {
        List<String> uuids = List.of("mockId");
        boolean hasSession = uuids.contains(uuid);

        Shape sessionShape = new Shape();
        Workflow workflowState = Workflow.builder().step(STEP.START).build();

        if(hasSession){
            sessionShape.generateRandomShape(); // obtainStoredShape
            workflowState.setStep(STEP.COMPLETE);
            workflowState.setShape(sessionShape); // obtainStoredWorkflow
        }

        return workflowState;
    }

    @PostMapping("/sendMessage")
    public Object sendMessage(@RequestBody Object test){
        return test;
    }
}
