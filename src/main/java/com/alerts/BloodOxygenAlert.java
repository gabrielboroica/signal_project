package com.alerts;

public class BloodOxygenAlert extends Alert {

    public BloodOxygenAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void trigger() {
        System.out.println("🫁 Blood Oxygen Alert for Patient " + getPatientId() + ": " + getCondition());
    }

    public static BloodOxygenAlert create(int patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
