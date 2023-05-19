package com.geometrypuzzle.backend.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Step;
import com.geometrypuzzle.backend.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Slf4j
@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public boolean hasSession(String uuid) {
        return storeRepository.findById(uuid).isPresent();
    }

    public Shape retrieveShape(String uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(StoreDoesNotExistException(uuid));
        return store.getShape();
    }

    public Step retrieveStep(String uuid) {
        Store store = storeRepository.findById(uuid).orElseThrow(StoreDoesNotExistException(uuid));
        return store.getStep();
    }

    public Store createStore(String uuid) {
        Store newStore = new Store();
        newStore.setProcessKey(uuid);
        newStore.setStep(Step.START);

        storeRepository.save(newStore);
        return newStore;
    }

    public void updateStore(Workflow workflow) {
        /* If session not found, create new
        * - .orElseGet is important here as it only runs if optional is not present unlike orElse */
        String uuid = workflow.getProcessKey();
        Store store = storeRepository.findById(uuid).orElseGet(() -> createStore(uuid));

        store.setStep(workflow.getStep());
        store.setShapeAsJson(getShapeJson(workflow));
        storeRepository.save(store);
    }
    private static Supplier<NoSuchElementException> StoreDoesNotExistException(String uuid) {
        return () -> new NoSuchElementException("Error fetching store for ID %s".formatted(uuid));
    }
    private static String getShapeJson(Workflow workflow) {
        // TODO: Check if JsonProcessingException gets thrown from mapped Exception
        try {
            return Utils.jsonify(workflow.getShape());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
