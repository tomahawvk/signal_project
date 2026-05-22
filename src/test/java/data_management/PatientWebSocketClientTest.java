package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.PatientWebSocketClient;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;

class PatientWebSocketClientTest {

    @Test
    void onMessageStoresParsedData() throws Exception {
        DataStorage storage = new DataStorage();
        PatientWebSocketClient client =
                new PatientWebSocketClient(new URI("ws://localhost:9999"), storage);

        client.onMessage("1,1000,Saturation,96%");

        List<PatientRecord> records = storage.getRecords(1, 0L, 2000L);

        assertEquals(1, records.size());
        assertEquals("Saturation", records.get(0).getRecordType());
        assertEquals(96.0, records.get(0).getMeasurementValue());
    }

    @Test
    void corruptedMessageAddsErrorMessage() throws Exception {
        DataStorage storage = new DataStorage();
        PatientWebSocketClient client =
                new PatientWebSocketClient(new URI("ws://localhost:9999"), storage);

        client.onMessage("bad-message");

        assertFalse(client.getErrorMessages().isEmpty());
    }

    @Test
    void onErrorStoresErrorMessage() throws Exception {
        DataStorage storage = new DataStorage();
        PatientWebSocketClient client =
                new PatientWebSocketClient(new URI("ws://localhost:9999"), storage);

        client.onError(new RuntimeException("network failed"));

        assertFalse(client.getErrorMessages().isEmpty());
    }
}