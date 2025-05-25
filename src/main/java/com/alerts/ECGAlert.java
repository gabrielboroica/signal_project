package com.alerts;

public class ECGAlert extends Alert {

    public ECGAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }

    @Override
    public void trigger() {
        System.out.println("❤️ ECG Alert for Patient " + getPatientId() + ": " + getCondition());
    }

    public static ECGAlert create(int patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
