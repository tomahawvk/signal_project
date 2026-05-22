package com.alerts;

/**
 * Decorator that marks an alert as repeatable.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    public RepeatedAlertDecorator(Alert wrappedAlert) {
        super(wrappedAlert);
    }

    @Override
    public boolean shouldRepeat() {
        return true;
    }
}
