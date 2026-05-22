package com.alerts;

public class ManualAlert extends BasicAlert {
    public ManualAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
