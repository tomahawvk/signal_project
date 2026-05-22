package com.alerts;

public class ManualAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        Alert alert = new ManualAlert(patientId, condition, timestamp);
        return new RepeatedAlertDecorator(alert);
    }
}