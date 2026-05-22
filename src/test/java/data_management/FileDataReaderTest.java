package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileDataReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void readsNumericPercentageAndAlertValues() throws IOException {
        Path file = tempDir.resolve("Saturation.txt");
        Files.writeString(file,
                "Patient ID: 1, Timestamp: 1000, Label: Saturation, Data: 96%\n"
                        + "Patient ID: 1, Timestamp: 2000, Label: SystolicPressure, Data: 120.0\n"
                        + "Patient ID: 1, Timestamp: 3000, Label: Alert, Data: triggered\n"
                        + "Patient ID: 1, Timestamp: 4000, Label: Alert, Data: resolved\n");

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(tempDir.toString());

        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 0L, 5000L);

        assertEquals(4, records.size());
        assertEquals(96.0, records.get(0).getMeasurementValue());
        assertEquals(120.0, records.get(1).getMeasurementValue());
        assertEquals(1.0, records.get(2).getMeasurementValue());
        assertEquals(0.0, records.get(3).getMeasurementValue());
    }
}