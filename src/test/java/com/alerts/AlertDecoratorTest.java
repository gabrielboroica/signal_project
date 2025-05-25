package com.alerts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertDecoratorTest {

    @Test
    public void testPriorityDecoratorTriggersWithoutException() {
        Alert baseAlert = BloodPressureAlert.create(2001, "HIGH_SYSTOLIC", 123456L);
        Alert priorityAlert = new PriorityAlertDecorator(baseAlert);

        assertDoesNotThrow(priorityAlert::trigger);
    }

    @Test
    public void testRepeatedDecoratorTriggersWithoutException() {
        Alert baseAlert = BloodOxygenAlert.create(2002, "LOW_OXYGEN", 123457L);
        Alert repeatedAlert = new RepeatedAlertDecorator(baseAlert);

        assertDoesNotThrow(repeatedAlert::trigger);
    }

    @Test
    public void testChainedDecoratorsTrigger() {
        Alert baseAlert = ECGAlert.create(2003, "IRREGULAR_RHYTHM", 123458L);
        Alert priorityAlert = new PriorityAlertDecorator(baseAlert);
        Alert repeatedPriorityAlert = new RepeatedAlertDecorator(priorityAlert);

        assertDoesNotThrow(repeatedPriorityAlert::trigger);
    }
}
