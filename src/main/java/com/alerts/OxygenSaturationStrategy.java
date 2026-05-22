package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.ArrayList;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000;

    private final AlertFactory oxygenFactory = new BloodOxygenAlertFactory();
    private final AlertFactory bloodPressureFactory = new BloodPressureAlertFactory();

    @Override
    public List<Alert> checkAlert(Patient patient, List<PatientRecord> records) {
        List<Alert> alerts = new ArrayList<>();

        checkLowSaturation(patient, records, alerts);
        checkRapidSaturationDrop(patient, records, alerts);
        checkHypotensiveHypoxemia(patient, records, alerts);

        return alerts;
    }

    private void checkLowSaturation(Patient patient, List<PatientRecord> records, List<Alert> alerts) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92) {
                alerts.add(oxygenFactory.createAlert(
                        String.valueOf(patient.getPatientId()),
                        "Low oxygen saturation",
                        record.getTimestamp()));
            }
        }
    }

    private void checkRapidSaturationDrop(Patient patient, List<PatientRecord> records, List<Alert> alerts) {
        List<PatientRecord> saturationRecords = filterByType(records, "Saturation");

        for (int i = 1; i < saturationRecords.size(); i++) {
            PatientRecord previous = saturationRecords.get(i - 1);
            PatientRecord current = saturationRecords.get(i);

            boolean withinTenMinutes = current.getTimestamp() - previous.getTimestamp() <= TEN_MINUTES_IN_MILLIS;
            boolean droppedByFive = previous.getMeasurementValue() - current.getMeasurementValue() >= 5;

            if (withinTenMinutes && droppedByFive) {
                alerts.add(oxygenFactory.createAlert(
                        String.valueOf(patient.getPatientId()),
                        "Rapid oxygen saturation drop",
                        current.getTimestamp()));
            }
        }
    }

    private void checkHypotensiveHypoxemia(Patient patient, List<PatientRecord> records, List<Alert> alerts) {
        boolean hasLowSystolic = false;
        boolean hasLowSaturation = false;
        long alertTimestamp = 0;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("SystolicPressure") && record.getMeasurementValue() < 90) {
                hasLowSystolic = true;
                alertTimestamp = Math.max(alertTimestamp, record.getTimestamp());
            }

            if (record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92) {
                hasLowSaturation = true;
                alertTimestamp = Math.max(alertTimestamp, record.getTimestamp());
            }
        }

        if (hasLowSystolic && hasLowSaturation) {
            alerts.add(bloodPressureFactory.createAlert(
                    String.valueOf(patient.getPatientId()),
                    "Hypotensive hypoxemia",
                    alertTimestamp));
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
}
