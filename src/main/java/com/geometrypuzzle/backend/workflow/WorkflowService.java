package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.store.Store;
import com.geometrypuzzle.backend.store.StoreService;
import com.geometrypuzzle.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkflowService {

    @Autowired
    private StoreService storeService;

    public Workflow processStartWorkflow(String uuid) {
        boolean hasSession = storeService.hasSession(uuid);

        Store store = hasSession ?
                storeService.retrieveStore(uuid) : newStore(uuid);

        return Workflow.builder()
                .processKey(uuid)
                .shape(store.getShape())
                .point(store.getPoint())
                .step(store.getStep())
                .build();
    }

    public Workflow processRestartWorkflow(String uuid) {
        Store store = storeService.safeRetrieveStore(uuid);

        store.setShapeJson(new Shape());
        store.setPointJson(new Point());
        store.setStep(Workflow.Step.START);
        storeService.updateStore(store);

        return Workflow.builder()
                .processKey(uuid)
                .shape(store.getShape())
                .point(store.getPoint())
                .step(store.getStep())
                .build();
    }

    public Workflow processContinueWorkflow(WorkflowController.ContinueRequest request) {
        Workflow workflow = retrieveWorkflow(request.getProcessKey());
        // Will revert to stored Step, if message is Null, if invalid String, will throw Exception.
        Workflow.Step step = Optional.ofNullable(request)
                .map(WorkflowController.ContinueRequest::getMessage)
                .map(WorkflowService::mapMessage)
                .orElseGet(workflow::getStep);

        // - TO Add workflow persistence
        //  e.g. limit allowed message calls for workflow integrity

        workflow.setStep(step);
        workflow.setPoint(request.getPoint());

        return workflow;
    }

    private static Workflow.Step mapMessage(WorkflowController.ContinueRequest.MessageName message) {
        return switch (message){
            case CUSTOM_SHAPE -> Workflow.Step.INCOMPLETE;
            case RANDOM_SHAPE -> Workflow.Step.RANDOM_SHAPE;
            case ADD_POINT -> Workflow.Step.ADD_POINT;
            case TEST_POINT -> Workflow.Step.TEST_POINT;
            case FINAL_SHAPE -> Workflow.Step.FINAL_SHAPE;
            case QUIT -> Workflow.Step.QUIT;
            default -> throw new IllegalArgumentException("Not a valid message name. %s".formatted(message));
        };
    }

    public Workflow retrieveWorkflow(String uuid) {
        boolean hasSession = storeService.hasSession(uuid);

        Store store = hasSession ?
                storeService.retrieveStore(uuid) : newStore(uuid);

        return Workflow.builder()
                .processKey(uuid)
                .shape(store.getShape())
                .step(store.getStep())
                .build();
    }

    public void updateWorkflow(String uuid, Puzzle puzzle) {
        Store store = storeService.retrieveStore(uuid);
        store.setShapeJson(puzzle.getShape());
        store.setStep(puzzle.getStoreStep());
        // Do not update null value, - for cases not ADD_POINT | TEST_POINT will wipe value
        // same as if(!null) store.setPointJson(puzzle.getStorePoint())
        Utils.consumeIfPresent(puzzle.getStorePoint(), store::setPointJson);

        storeService.updateStore(store);
    }

    private Store newStore(String uuid) {
        Store newStore = storeService.createStore(uuid);
        newStore.setShapeJson(new Shape());
        newStore.setStep(Workflow.Step.START);
        storeService.updateStore(newStore);

        return newStore;
    }
}
