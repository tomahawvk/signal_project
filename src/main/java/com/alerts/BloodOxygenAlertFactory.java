package com.alerts;

public class BloodOxygenAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        Alert alert = new BloodOxygenAlert(patientId, condition, timestamp);

        if (condition.contains("Rapid")) {
            return new RepeatedAlertDecorator(alert);
        }

        if (condition.contains("Low")) {
            return new PriorityAlertDecorator(alert, "HIGH");
        }

        return alert;
    }
}