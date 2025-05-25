package com.alerts;

import com.alerts.AlertStrategy;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(double value) {
        // Alertă dacă pulsul este < 50 sau > 110
        return value < 50 || value > 110;
    }
}
