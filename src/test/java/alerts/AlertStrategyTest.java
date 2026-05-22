package alerts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alerts.Alert;
import com.alerts.BloodPressureStrategy;
import com.alerts.HeartRateStrategy;
import com.alerts.ManualAlertStrategy;
import com.alerts.OxygenSaturationStrategy;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;
import org.junit.jupiter.api.Test;

class AlertStrategyTest {

    @Test
    void bloodPressureStrategyFindsCriticalPressure() {
        Patient patient = new Patient(1);
        patient.addRecord(181.0, "SystolicPressure", 1000L);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        List<Alert> alerts = strategy.checkAlert(patient, patient.getRecords(0, Long.MAX_VALUE));

        assertHasCondition(alerts, "Critical high systolic pressure");
    }

    @Test
    void oxygenStrategyFindsLowSaturation() {
        Patient patient = new Patient(1);
        patient.addRecord(91.0, "Saturation", 1000L);

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        List<Alert> alerts = strategy.checkAlert(patient, patient.getRecords(0, Long.MAX_VALUE));

        assertHasCondition(alerts, "Low oxygen saturation");
    }

    @Test
    void heartRateStrategyFindsAbnormalEcgPeak() {
        Patient patient = new Patient(1);
        patient.addRecord(0.4, "ECG", 1000L);
        patient.addRecord(0.5, "ECG", 2000L);
        patient.addRecord(0.4, "ECG", 3000L);
        patient.addRecord(2.0, "ECG", 4000L);

        HeartRateStrategy strategy = new HeartRateStrategy();
        List<Alert> alerts = strategy.checkAlert(patient, patient.getRecords(0, Long.MAX_VALUE));

        assertHasCondition(alerts, "Abnormal ECG peak");
    }

    @Test
    void manualStrategyFindsManualAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "Alert", 1000L);

        ManualAlertStrategy strategy = new ManualAlertStrategy();
        List<Alert> alerts = strategy.checkAlert(patient, patient.getRecords(0, Long.MAX_VALUE));

        assertHasCondition(alerts, "Manual alert triggered");
    }

    private void assertHasCondition(List<Alert> alerts, String condition) {
        boolean found = false;

        for (Alert alert : alerts) {
            if (alert.getCondition().equals(condition)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Expected alert condition: " + condition);
    }
}
