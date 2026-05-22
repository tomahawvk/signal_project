package com.data_management;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * WebSocket client that receives real-time patient data and stores it.
 */
public class PatientWebSocketClient extends WebSocketClient {
    private final DataStorage dataStorage;
    private final DataMessageParser parser;
    private final List<String> errorMessages = new ArrayList<>();

    public PatientWebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
        this.parser = new DataMessageParser();
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        try {
            PatientRecord record = parser.parse(message);
            dataStorage.addPatientData(
                    record.getPatientId(),
                    record.getMeasurementValue(),
                    record.getRecordType(),
                    record.getTimestamp());
        } catch (IllegalArgumentException e) {
            errorMessages.add("Invalid message: " + message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        errorMessages.add("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        errorMessages.add("WebSocket error: " + ex.getMessage());
    }

    public List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages);
    }
}