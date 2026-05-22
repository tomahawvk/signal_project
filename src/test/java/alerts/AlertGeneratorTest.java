package alerts;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

class AlertGeneratorTest {

    @Test
    void highSystolicPressureCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(181.0, "SystolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Critical high systolic pressure");
    }

    @Test
    void lowSystolicPressureCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(89.0, "SystolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Critical low systolic pressure");
    }

    @Test
    void highDiastolicPressureCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(121.0, "DiastolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Critical high diastolic pressure");
    }

    @Test
    void lowDiastolicPressureCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(59.0, "DiastolicPressure", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Critical low diastolic pressure");
    }

    @Test
    void lowSaturationCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(91.0, "Saturation", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Low oxygen saturation");
    }

    @Test
    void rapidSaturationDropCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(98.0, "Saturation", 1000L);
        patient.addRecord(93.0, "Saturation", 1000L + 5 * 60 * 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Rapid oxygen saturation drop");
    }

    @Test
    void hypotensiveHypoxemiaCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(89.0, "SystolicPressure", 1000L);
        patient.addRecord(91.0, "Saturation", 2000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Hypotensive hypoxemia");
    }

    @Test
    void manualAlertCreatesAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(1.0, "Alert", 1000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertHasAlert(generator, "Manual alert triggered");
    }

    @Test
    void normalDataCreatesNoAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(120.0, "SystolicPressure", 1000L);
        patient.addRecord(80.0, "DiastolicPressure", 2000L);
        patient.addRecord(97.0, "Saturation", 3000L);

        AlertGenerator generator = new AlertGenerator(new DataStorage());
        generator.evaluateData(patient);

        assertTrue(generator.getTriggeredAlerts().isEmpty());
    }

    private void assertHasAlert(AlertGenerator generator, String condition) {
        boolean found = false;

        for (Alert alert : generator.getTriggeredAlerts()) {
            if (alert.getCondition().equals(condition)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Expected alert condition: " + condition);
    }
}
