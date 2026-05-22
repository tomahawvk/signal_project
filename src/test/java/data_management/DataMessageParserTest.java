package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.data_management.DataMessageParser;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;

class DataMessageParserTest {

    @Test
    void parsesNumericMessage() {
        DataMessageParser parser = new DataMessageParser();

        PatientRecord record = parser.parse("1,1000,SystolicPressure,120.0");

        assertEquals(1, record.getPatientId());
        assertEquals(1000L, record.getTimestamp());
        assertEquals("SystolicPressure", record.getRecordType());
        assertEquals(120.0, record.getMeasurementValue());
    }

    @Test
    void parsesPercentageMessage() {
        DataMessageParser parser = new DataMessageParser();

        PatientRecord record = parser.parse("1,1000,Saturation,96%");

        assertEquals(96.0, record.getMeasurementValue());
    }

    @Test
    void parsesTriggeredMessage() {
        DataMessageParser parser = new DataMessageParser();

        PatientRecord record = parser.parse("1,1000,Alert,triggered");

        assertEquals(1.0, record.getMeasurementValue());
    }

    @Test
    void parsesResolvedMessage() {
        DataMessageParser parser = new DataMessageParser();

        PatientRecord record = parser.parse("1,1000,Alert,resolved");

        assertEquals(0.0, record.getMeasurementValue());
    }

    @Test
    void rejectsCorruptedMessage() {
        DataMessageParser parser = new DataMessageParser();

        assertThrows(IllegalArgumentException.class, () -> parser.parse("bad-message"));
    }
}
