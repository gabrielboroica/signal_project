package com.alerts;

public abstract class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    private String message;

    public Alert(int patientId, String condition, long timestamp ) {
        this.patientId = String.valueOf(patientId);
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public abstract void trigger();

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
