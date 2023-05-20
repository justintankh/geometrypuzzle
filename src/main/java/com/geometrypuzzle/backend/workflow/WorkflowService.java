package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.session.Store;
import com.geometrypuzzle.backend.session.StoreService;
import com.geometrypuzzle.backend.shape.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .step(store.getStep())
                .build();
    }

    public Workflow processRestartWorkflow(String uuid) {
        Store store = storeService.safeRetrieveStore(uuid);

        store.setShapeJson(new Shape());
        store.setStep(Step.START);
        storeService.updateStore(store);

        return Workflow.builder()
                .processKey(uuid)
                .shape(store.getShape())
                .step(store.getStep())
                .build();
    }

    public Workflow processContinueWorkflow(WorkflowController.ContinueRequest request) {
        Workflow workflow = retrieveWorkflow(request.getProcessKey());
        // Will revert to stored Step, if message is Null, if invalid String, will throw Exception.
        Step step = Optional.ofNullable(request)
                .map(WorkflowController.ContinueRequest::getMessage)
                .map(WorkflowService::mapMessage)
                .orElseGet(workflow::getStep);

        // - TO Add workflow persistence
        //  e.g. limit illegal message calls

        workflow.setStep(step);
        workflow.setPoint(request.getPoint());

        return workflow;
    }

    private static Step mapMessage(String message) {
        return switch (message){
            case "CUSTOM_SHAPE" -> Step.INCOMPLETE;
            case "RANDOM_SHAPE" -> Step.RANDOM_SHAPE;
            case "ADD_POINT" -> Step.ADD_POINT;
            case "TEST_POINT" -> Step.TEST_POINT;
            case "FINAL_SHAPE" -> Step.FINAL_SHAPE;
            case "QUIT" -> Step.QUIT;
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
        storeService.updateStore(store);
    }

    private Store newStore(String uuid) {
        Store newStore = storeService.createStore(uuid);
        newStore.setShapeJson(new Shape());
        newStore.setStep(Step.START);
        storeService.updateStore(newStore);

        return newStore;
    }

    /* for debugging, to be removed */
    public List<Store> getAllStore() {
        return storeService.retrieveAllStore();
    }
}
