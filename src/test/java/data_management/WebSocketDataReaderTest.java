package data_management;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.data_management.WebSocketDataReader;
import org.junit.jupiter.api.Test;

class WebSocketDataReaderTest {

    @Test
    void rejectsInvalidUri() {
        assertThrows(IllegalArgumentException.class, () -> new WebSocketDataReader("not a uri"));
    }

    @Test
    void stopBeforeStartDoesNotFail() {
        WebSocketDataReader reader = new WebSocketDataReader("ws://localhost:9999");

        reader.stop();
    }
}