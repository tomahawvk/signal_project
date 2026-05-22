package com.alerts;

/**
 * Base decorator for dynamically extending alert behavior.
 */
public abstract class AlertDecorator implements Alert {
    private final Alert wrappedAlert;

    protected AlertDecorator(Alert wrappedAlert) {
        this.wrappedAlert = wrappedAlert;
    }

    @Override
    public String getPatientId() {
        return wrappedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return wrappedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return wrappedAlert.getTimestamp();
    }

    @Override
    public String getPriority() {
        return wrappedAlert.getPriority();
    }

    @Override
    public boolean shouldRepeat() {
        return wrappedAlert.shouldRepeat();
    }
}