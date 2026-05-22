package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
    private final AlertFactory alertFactory = new ECGAlertFactory();

    @Override
    public List<Alert> checkAlert(Patient patient, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();
        List<PatientRecord> ecgRecords = filterByType(records, "ECG");

        for (int i = 3; i < ecgRecords.size(); i++) {
            double average =
                    (ecgRecords.get(i - 3).getMeasurementValue()
                            + ecgRecords.get(i - 2).getMeasurementValue()
                            + ecgRecords.get(i - 1).getMeasurementValue())
                            / 3.0;

            double current = ecgRecords.get(i).getMeasurementValue();

            if (current > average * 2 && current > 1.0) {
                alerts.add(alertFactory.createAlert(
                        String.valueOf(patient.getPatientId()),
                        "Abnormal ECG peak",
                        ecgRecords.get(i).getTimestamp()));
            }
        }

        return alerts;
    }

    private List<PatientRecord> filterByType(List<PatientRecord> records, String type) {
        List<PatientRecord> filteredRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equals(type)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }
}
