package com.alerts;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        Alert alert = new ECGAlert(patientId, condition, timestamp);
        return new PriorityAlertDecorator(alert, "HIGH");
    }
}