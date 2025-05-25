package com.alerts;

public class BloodPressureAlert extends Alert {

    public BloodPressureAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void trigger() {
        System.out.println("🚨 Blood Pressure Alert for Patient " + getPatientId() + ": " + getCondition());
    }

    // 🔧 Factory Method integrat în aceeași clasă
    public static BloodPressureAlert create(int patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp);
    }
}
