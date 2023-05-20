package com.geometrypuzzle.backend.workflow;

import com.geometrypuzzle.backend.point.Point;
import com.geometrypuzzle.backend.puzzle.Puzzle;
import com.geometrypuzzle.backend.shape.Shape;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

@AutoConfigureMockMvc
class WorkflowFactoryTest {
    WorkflowFactory undertest;
    Workflow mockWorkflow;
    @BeforeEach
    void setup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        undertest = new WorkflowFactory();
        mockWorkflow = Workflow.builder()
                .shape(new Shape())
                .point(new Point())
                .build();

        /* Mock post construct for factory handler */
        Method postConstruct = undertest.getClass().getDeclaredMethod("configureFactory");
        postConstruct.setAccessible(true);
        postConstruct.invoke(undertest);
    }

    public static Stream<Arguments> storeDataTestData() {
        return Stream.of(
                TestUtils.Setup.builder()
                        .testDesc("Test START expected message")
                        .testMessage("Welcome to the GIC geometry puzzle app")
                        .workflowConsumer(TestUtils.Setup.stepConsumerSetter(Workflow.Step.START))
                        .build()
                ,
                TestUtils.Setup.builder()
                        .testDesc("Test RANDOM expected message")
                        .testMessage("Your random shape is")
                        .workflowConsumer(TestUtils.Setup.stepConsumerSetter(Workflow.Step.RANDOM_SHAPE))
                        .build()
        ).map(Arguments::of);
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("storeDataTestData")
    void startWorkflow(TestUtils.Setup testSetup) {
        // ARRANGE
        testSetup.runArrange(mockWorkflow);
        // ACT
        Puzzle result = undertest.triggerService(mockWorkflow);
        // ASSERT
        Assertions.assertEquals(result.getPuzzleDisplay().getDisplayMessage(), testSetup.getTestMessage());
    }

    @Test
    void restartWorkflow() {
        // ARRANGE
        mockWorkflow.setStep(Workflow.Step.START);

        // ACT
        Puzzle result = undertest.triggerService(mockWorkflow);

        // ASSERT
        Assertions.assertEquals(result.getPuzzleDisplay().getDisplayMessage(), "Welcome to the GIC geometry puzzle app");
    }

    @Test
    void continueWorkflow() {
    }
}