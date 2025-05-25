package com.alerts;

import com.alerts.AlertStrategy;

public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public boolean checkAlert(double value) {
        // Exemplu: valori peste 140 se consideră periculoase
        return value > 140;
    }
}
