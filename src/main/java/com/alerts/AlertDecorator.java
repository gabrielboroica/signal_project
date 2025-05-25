package com.alerts;

public abstract class AlertDecorator extends Alert {
    protected Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        super(Integer.parseInt(decoratedAlert.getPatientId()),
                decoratedAlert.getCondition(),
                decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public abstract void trigger();
}
