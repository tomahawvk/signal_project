package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class ManualAlertStrategy implements AlertStrategy {
    private final AlertFactory alertFactory = new ManualAlertFactory();

    @Override
    public List<Alert> checkAlert(Patient patient, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Alert") && record.getMeasurementValue() == 1.0) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(patient.getPatientId()),
                        "Manual alert triggered",
                        record.getTimestamp()));
            }
        }

        return alerts;
    }
}
