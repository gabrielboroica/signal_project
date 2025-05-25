package com.cardio_generator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HealthDataSimulatorTest {

    @Test
    public void testSingletonInstance() {
        HealthDataSimulator sim1 = HealthDataSimulator.getInstance();
        HealthDataSimulator sim2 = HealthDataSimulator.getInstance();

        assertSame(sim1, sim2);
    }
}
