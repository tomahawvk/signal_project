package com.data_management;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * DataReader implementation for real-time WebSocket data streams.
 */
public class WebSocketDataReader implements DataReader {
    private final URI serverUri;
    private PatientWebSocketClient client;

    public WebSocketDataReader(String uri) {
        try {
            this.serverUri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid WebSocket URI: " + uri, e);
        }
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        start(dataStorage);
    }

    @Override
    public void start(DataStorage dataStorage) throws IOException {
        client = new PatientWebSocketClient(serverUri, dataStorage);
        client.connect();
    }

    @Override
    public void stop() {
        if (client != null) {
            client.close();
        }
    }

    public PatientWebSocketClient getClient() {
        return client;
    }
}
