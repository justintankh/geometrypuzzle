package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.session.Store;
import com.geometrypuzzle.backend.session.StoreService;
import com.geometrypuzzle.backend.shape.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowService {

    @Autowired
    private StoreService storeService;

    public Workflow restartWorkflow(String uuid) {
        boolean hasSession = storeService.hasSession(uuid);

        Store store = hasSession ?
                storeService.retrieveStore(uuid) : newStore(uuid);

        store.setShapeAsJson(new Shape());
        store.setStep(Step.START);
        storeService.updateStore(store);

        return Workflow.builder()
                .processKey(uuid)
                .shape(store.getShape())
                .step(store.getStep())
                .build();
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
        store.setShapeAsJson(puzzle.getShape());
        store.setStep(puzzle.getNextStep());
        storeService.updateStore(store);
    }

    private Store newStore(String uuid) {
        Store newStore = storeService.createStore(uuid);
        newStore.setShapeAsJson(new Shape());
        newStore.setStep(Step.START);
        storeService.updateStore(newStore);

        return newStore;
    }

    /* for debugging, to be removed */
    public List<Store> getAllStore() {
        return storeService.retrieveAllStore();
    }
}
