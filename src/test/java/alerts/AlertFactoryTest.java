package alerts;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alerts.Alert;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodOxygenAlertFactory;
import com.alerts.BloodPressureAlert;
import com.alerts.BloodPressureAlertFactory;
import com.alerts.ECGAlert;
import com.alerts.ECGAlertFactory;
import com.alerts.ManualAlert;
import com.alerts.ManualAlertFactory;
import org.junit.jupiter.api.Test;

class AlertFactoryTest {

    @Test
    void bloodPressureFactoryCreatesBloodPressureAlert() {
        Alert alert = new BloodPressureAlertFactory()
                .createAlert("1", "SystolicPressure increasing trend", 1000L);

        assertInstanceOf(BloodPressureAlert.class, alert);
        assertEquals("1", alert.getPatientId());
        assertEquals("SystolicPressure increasing trend", alert.getCondition());
        assertEquals(1000L, alert.getTimestamp());
    }

    @Test
    void bloodOxygenFactoryCreatesBloodOxygenAlert() {
        Alert alert = new BloodOxygenAlertFactory()
                .createAlert("1", "Normal oxygen observation", 1000L);

        assertInstanceOf(BloodOxygenAlert.class, alert);
    }

    @Test
    void ecgFactoryCreatesDecoratedEcgAlert() {
        Alert alert = new ECGAlertFactory()
                .createAlert("1", "Abnormal ECG peak", 1000L);

        assertEquals("HIGH", alert.getPriority());
        assertEquals("Abnormal ECG peak", alert.getCondition());
    }

    @Test
    void manualFactoryCreatesRepeatableManualAlert() {
        Alert alert = new ManualAlertFactory()
                .createAlert("1", "Manual alert triggered", 1000L);

        assertEquals("Manual alert triggered", alert.getCondition());
        assertEquals(true, alert.shouldRepeat());
    }
}
