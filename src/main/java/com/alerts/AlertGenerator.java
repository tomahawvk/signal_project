package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.BasicAlert;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    // Google Java Style: Use final for fields that are assigned once and never reassigned.
    private final DataStorage dataStorage;
    private final List<Alert> triggeredAlerts = new ArrayList<>();
    private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Implementation goes here
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        records.sort(Comparator.comparingLong(PatientRecord::getTimestamp));

        checkCriticalBloodPressure(patient, records);
        checkBloodPressureTrend(patient, records, "SystolicPressure");
        checkBloodPressureTrend(patient, records, "DiastolicPressure");
        checkLowSaturation(patient, records);
        checkRapidSaturationDrop(patient, records);
        checkHypotensiveHypoxemia(patient, records);
        checkEcgPeak(patient, records);
        checkManualAlert(patient, records);
    }

    private void checkCriticalBloodPressure(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();

            if (type.equals("SystolicPressure") && value > 180) {
                triggerAlert(patient, "Critical high systolic pressure", record.getTimestamp());
            } else if (type.equals("SystolicPressure") && value < 90) {
                triggerAlert(patient, "Critical low systolic pressure", record.getTimestamp());
            } else if (type.equals("DiastolicPressure") && value > 120) {
                triggerAlert(patient, "Critical high diastolic pressure", record.getTimestamp());
            } else if (type.equals("DiastolicPressure") && value < 60) {
                triggerAlert(patient, "Critical low diastolic pressure", record.getTimestamp());
            }
        }
    }

    private void checkBloodPressureTrend(Patient patient, List<PatientRecord> records, String type) {
    List<PatientRecord> pressureRecords = filterByType(records, type);

    for (int i = 2; i < pressureRecords.size(); i++) {
        double first = pressureRecords.get(i - 2).getMeasurementValue();
        double second = pressureRecords.get(i - 1).getMeasurementValue();
        double third = pressureRecords.get(i).getMeasurementValue();

        boolean increasing = second - first > 10 && third - second > 10;
        boolean decreasing = first - second > 10 && second - third > 10;

        if (increasing) {
            triggerAlert(patient, type + " increasing trend", pressureRecords.get(i).getTimestamp());
        }

        if (decreasing) {
            triggerAlert(patient, type + " decreasing trend", pressureRecords.get(i).getTimestamp());
        }
    }
}

    private void checkLowSaturation(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Saturation") && record.getMeasurementValue() < 92) {
                triggerAlert(patient, "Low oxygen saturation", record.getTimestamp());
            }
        }
    }

    private void checkRapidSaturationDrop(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> saturationRecords = filterByType(records, "Saturation");

        for (int i = 1; i < saturationRecords.size(); i++) {
            PatientRecord previous = saturationRecords.get(i - 1);
            PatientRecord current = saturationRecords.get(i);

            boolean withinTenMinutes = current.getTimestamp() - previous.getTimestamp() <= TEN_MINUTES_IN_MILLIS;
            boolean droppedByFive = previous.getMeasurementValue() - current.getMeasurementValue() >= 5;

            if (withinTenMinutes && droppedByFive) {
                triggerAlert(patient, "Rapid oxygen saturation drop", current.getTimestamp());
            }
        }
    }

    private void checkHypotensiveHypoxemia(Patient patient, List<PatientRecord> records) {
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
            triggerAlert(patient, "Hypotensive hypoxemia", alertTimestamp);
        }
    }

    private void checkEcgPeak(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> ecgRecords = filterByType(records, "ECG");

        for (int i = 3; i < ecgRecords.size(); i++) {
            double average =
                    (ecgRecords.get(i - 3).getMeasurementValue()
                            + ecgRecords.get(i - 2).getMeasurementValue()
                            + ecgRecords.get(i - 1).getMeasurementValue())
                            / 3.0;

            double current = ecgRecords.get(i).getMeasurementValue();

            if (current > average * 2 && current > 1.0) {
                triggerAlert(patient, "Abnormal ECG peak", ecgRecords.get(i).getTimestamp());
            }
        }
    }

    private void checkManualAlert(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Alert") && record.getMeasurementValue() == 1.0) {
                triggerAlert(patient, "Manual alert triggered", record.getTimestamp());
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

    private void triggerAlert(Patient patient, String condition, long timestamp) {
        Alert alert = new BasicAlert(String.valueOf(patient.getPatientId()), condition, timestamp);
        triggerAlert(alert);
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        triggeredAlerts.add(alert);
    }

    public List<Alert> getTriggeredAlerts() {
        return new ArrayList<>(triggeredAlerts);
    }
}
