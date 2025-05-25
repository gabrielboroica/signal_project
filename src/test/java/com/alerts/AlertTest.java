package com.alerts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertTest {

    @Test
    void testConstructorAndGetters_WithValidData() {
        String expectedPatientId = "123";
        String condition = "Low Oxygen";
        long timestamp = 1650000000L;
        String message = "Critical oxygen drop";

        // Option 1: Constructor with message (if added later)
        Alert alert = new Alert(123, condition, timestamp);
        // alert = new Alert(123, condition, timestamp, message); // If you add the extra param

        assertEquals(expectedPatientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());

        // Depending on how you handle `message`, adjust this check:
        // If you fix the constructor to accept message, check it directly:
        // assertEquals(message, alert.getMessage());

        // If message remains null (as in original code):
        assertNull(alert.getMessage());
    }

    @Test
    void testDifferentPatientIdConversion() {
        Alert alert = new Alert(999, "High BP", 1700000000L);
        assertEquals("999", alert.getPatientId());
    }
}
