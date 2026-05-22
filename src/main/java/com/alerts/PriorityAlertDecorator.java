package com.alerts;

/**
 * Decorator that adds priority behavior to an alert.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private final String priority;

    public PriorityAlertDecorator(Alert wrappedAlert, String priority) {
        super(wrappedAlert);
        this.priority = priority;
    }

    @Override
    public String getPriority() {
        return priority;
    }
}
