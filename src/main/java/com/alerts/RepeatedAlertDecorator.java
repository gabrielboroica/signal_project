package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {

    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public void trigger() {
        decoratedAlert.trigger();
        System.out.println("🔁 Repeating alert for patient " + getPatientId());
    }
}
