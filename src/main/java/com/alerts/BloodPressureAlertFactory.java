package com.alerts;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        Alert alert = new BloodPressureAlert(patientId, condition, timestamp);

        if (condition.contains("Critical") || condition.contains("Hypotensive")) {
            return new PriorityAlertDecorator(alert, "HIGH");
        }

        return alert;
    }
}
