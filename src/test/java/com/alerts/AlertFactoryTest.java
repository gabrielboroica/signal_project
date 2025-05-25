package com.alerts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertFactoryTest {

    @Test
    public void testBloodPressureAlertFactory() {
        Alert alert = BloodPressureAlert.create(1001, "HIGH_SYSTOLIC", 123456789L);

        assertNotNull(alert);
        assertEquals("1001", alert.getPatientId());
        assertEquals("HIGH_SYSTOLIC", alert.getCondition());
        assertEquals(123456789L, alert.getTimestamp());
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        Alert alert = BloodOxygenAlert.create(1002, "LOW_OXYGEN", 123456790L);

        assertNotNull(alert);
        assertEquals("1002", alert.getPatientId());
        assertEquals("LOW_OXYGEN", alert.getCondition());
        assertEquals(123456790L, alert.getTimestamp());
    }

    @Test
    public void testECGAlertFactory() {
        Alert alert = ECGAlert.create(1003, "IRREGULAR_RHYTHM", 123456791L);

        assertNotNull(alert);
        assertEquals("1003", alert.getPatientId());
        assertEquals("IRREGULAR_RHYTHM", alert.getCondition());
        assertEquals(123456791L, alert.getTimestamp());
    }
}
