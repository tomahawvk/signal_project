package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    private final AlertFactory alertFactory = new BloodPressureAlertFactory();

    @Override
    public List<Alert> checkAlert(Patient patient, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        checkCriticalBloodPressure(patient, records, alerts);
        checkBloodPressureTrend(patient, records, alerts, "SystolicPressure");
        checkBloodPressureTrend(patient, records, alerts, "DiastolicPressure");

        return alerts;
    }

    private void checkCriticalBloodPressure(
            Patient patient, List<PatientRecord> records, List<Alert> alerts) {
        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("SystolicPressure") && value > 180) {
                alerts.add(createAlert(patient, "Critical high systolic pressure", record.getTimestamp()));
            } else if (type.equals("SystolicPressure") && value < 90) {
                alerts.add(createAlert(patient, "Critical low systolic pressure", record.getTimestamp()));
            } else if (type.equals("DiastolicPressure") && value > 120) {
                alerts.add(createAlert(patient, "Critical high diastolic pressure", record.getTimestamp()));
            } else if (type.equals("DiastolicPressure") && value < 60) {
                alerts.add(createAlert(patient, "Critical low diastolic pressure", record.getTimestamp()));
            }
        }
    }

    private void checkBloodPressureTrend(
            Patient patient, List<PatientRecord> records, List<Alert> alerts, String type) {
        List<PatientRecord> pressureRecords = filterByType(records, type);

        for (int i = 2; i < pressureRecords.size(); i++) {
            double first = pressureRecords.get(i - 2).getMeasurementValue();
            double second = pressureRecords.get(i - 1).getMeasurementValue();
            double third = pressureRecords.get(i).getMeasurementValue();

            boolean increasing = second - first > 10 && third - second > 10;
            boolean decreasing = first - second > 10 && second - third > 10;

            if (increasing) {
                alerts.add(createAlert(patient, type + " increasing trend", pressureRecords.get(i).getTimestamp()));
            }

            if (decreasing) {
                alerts.add(createAlert(patient, type + " decreasing trend", pressureRecords.get(i).getTimestamp()));
            }
        }
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

    private Alert createAlert(Patient patient, String condition, long timestamp) {
        return alertFactory.createAlert(String.valueOf(patient.getPatientId()), condition, timestamp);
    }
}
