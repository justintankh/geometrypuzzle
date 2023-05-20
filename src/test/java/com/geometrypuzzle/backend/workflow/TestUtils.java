package com.geometrypuzzle.backend.workflow;

import lombok.Builder;
import lombok.Data;

import java.util.function.Consumer;

public class TestUtils {
    @Builder
    @Data
    public static class Setup {
        String testDesc;
        Consumer<Workflow> workflowConsumer;
        String testMessage;
        String testInstructions;

        public void runArrange(Workflow workflow) {
            this.workflowConsumer.accept(workflow);
        }
        @Override
        public String toString() {
            return this.testDesc;
        }
        public static Consumer<Workflow> stepConsumerSetter(Workflow.Step step) {
            return (Workflow -> Workflow.setStep(step));
        }
    }
}
