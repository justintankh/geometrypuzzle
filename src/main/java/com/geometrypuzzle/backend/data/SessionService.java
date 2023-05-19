package com.geometrypuzzle.backend.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.geometrypuzzle.backend.shape.Shape;
import com.geometrypuzzle.backend.utils.Utils;
import com.geometrypuzzle.backend.workflow.Step;
import com.geometrypuzzle.backend.workflow.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public boolean hasSession(String uuid) {
        return sessionRepository.findById(uuid).isPresent();
    }

    public Shape retrieveShape(String uuid) {
        Shape storedShape = sessionRepository.findById(uuid)
                .map(SessionService::getShapeException)
                .orElseThrow(() -> new RuntimeException("Shape not found in database for ID %s".formatted(uuid)));

        return storedShape;
    }

    public Step retrieveStep(String uuid) {
        Step storedStep = sessionRepository.findById(uuid)
                .map(Session::getStep)
                .orElseThrow(() -> new RuntimeException("Step not found in database for ID %s".formatted(uuid)));

        return storedStep;
    }

    public Session createStore(String uuid) {
        Session newSession = new Session();
        newSession.setBusinessKey(uuid);
        newSession.setStep(Step.START);
        sessionRepository.save(newSession);
        return newSession;
    }

    public void updateStore(Workflow workflow) {
        /* If session not found, create new
        * - .orElseGet is important here as it only runs if optional is not present unlike orElse */
        String uuid = workflow.getBusinessKey();
        Session store = sessionRepository.findById(uuid).orElseGet(() -> createStore(uuid));
        store.setStep(workflow.getStep());
        store.setShapeAsJson(getShapeJson(workflow));
        sessionRepository.save(store);
    }

    private static Shape getShapeException(Session session) {
        // TODO: Check if JsonProcessingException gets thrown from mapped Exception
        try {
            return session.getShape();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
