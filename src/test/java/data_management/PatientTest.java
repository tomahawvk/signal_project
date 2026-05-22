package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void getRecordsReturnsOnlyRecordsInsideTimeRange() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "SystolicPressure", 1000L);
        patient.addRecord(110.0, "SystolicPressure", 2000L);
        patient.addRecord(120.0, "SystolicPressure", 3000L);

        List<PatientRecord> records = patient.getRecords(1500L, 2500L);

        assertEquals(1, records.size());
        assertEquals(110.0, records.get(0).getMeasurementValue());
    }

    @Test
    void getRecordsReturnsEmptyListWhenNoRecordsMatch() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "SystolicPressure", 1000L);

        List<PatientRecord> records = patient.getRecords(2000L, 3000L);

        assertTrue(records.isEmpty());
    }
}
