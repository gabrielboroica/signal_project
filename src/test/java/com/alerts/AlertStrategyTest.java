package com.alerts;

import com.alerts.BloodPressureStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AlertStrategyTest {

    @Test
    public void testBloodPressureStrategy() {
        AlertStrategy strategy = new BloodPressureStrategy();
        assertTrue(strategy.checkAlert(150));   // peste limită
        assertFalse(strategy.checkAlert(120));  // în regulă
    }

    @Test
    public void testOxygenSaturationStrategy() {
        AlertStrategy strategy = new OxygenSaturationStrategy();
        assertTrue(strategy.checkAlert(88));    // sub limită
        assertFalse(strategy.checkAlert(96));   // în regulă
    }

    @Test
    public void testHeartRateStrategy() {
        AlertStrategy strategy = new HeartRateStrategy();
        assertTrue(strategy.checkAlert(45));    // prea mic
        assertTrue(strategy.checkAlert(120));   // prea mare
        assertFalse(strategy.checkAlert(75));   // normal
    }


}
