package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.puzzle.PuzzleDisplay;
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

    @GetMapping
    public String test() {
        return "hello world";
    }

    @PostMapping("/startWorkflow")
    public Puzzle startWorkflow(@RequestBody String uuid) {
        boolean hasSession = isHasSession(uuid);
        Shape sessionShape = null;

        PuzzleDisplay puzzleDisplay = PuzzleDisplay.builder()
                .displayBanner(null)
                .displayMessage("mockMessage")
                .displayInstructions("[1] Create a custom shape\n" + "[2] Create a random shape")
                .build();

        if(hasSession){
            /* Get puzzle return from factory */
            sessionShape = getShape(); // obtainStoredShape
            puzzleDisplay = PuzzleDisplay.builder()
                    .displayBanner(null)
                    .displayInstructions("mockInstructions")
                    .displayMessage("mockMessage").build();
        }

        return Puzzle.builder()
                .puzzleDisplay(puzzleDisplay)
                .shape(sessionShape).build();
    }

    @PostMapping("/sendMessage")
    public Puzzle sendMessage(@RequestBody RequestDetails request){
        boolean hasSession = isHasSession(request.getUuid());

        // Reconstruct Shape
        Shape retrievedShape = hasSession ? getShape() : new Shape();
        
        // Construct factory workflow
        Workflow workflow = Workflow.builder()
                .step(request.getStep())
                .shape(retrievedShape)
                .build();

        WorkflowFactory factory = new WorkflowFactory(workflow);
        return factory.triggerService();
    }
    
    private static Shape getShape() {
        Shape sessionShape = new Shape();
        sessionShape.generateRandomShape();
        return sessionShape;
    }
    private static boolean isHasSession(String uuid) {
        List<String> uuids = List.of("mockId");
        boolean hasSession = uuids.contains(uuid);
        return hasSession;
    }
}

@Data
class RequestDetails {
    String uuid;
    STEP step;
    Point point;
}