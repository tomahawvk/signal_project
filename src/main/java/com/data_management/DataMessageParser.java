package com.data_management;

/**
 * Parses real-time patient data messages from WebSocket streams.
 */
public class DataMessageParser {

    /**
     * Parses a CSV message in the format patientId,timestamp,label,data.
     *
     * @param message the incoming WebSocket message
     * @return a patient record parsed from the message
     * @throws IllegalArgumentException if the message format or value is invalid
     */
    public PatientRecord parse(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        String[] parts = message.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid message format: " + message);
        }

        try {
            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String label = parts[2].trim();
            String rawData = parts[3].trim();

            double value = parseValue(rawData);
            return new PatientRecord(patientId, value, label, timestamp);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in message: " + message, e);
        }
    }

    private double parseValue(String rawData) {
        if (rawData.endsWith("%")) {
            return Double.parseDouble(rawData.replace("%", ""));
        }

        if (rawData.equalsIgnoreCase("triggered")) {
            return 1.0;
        }

        if (rawData.equalsIgnoreCase("resolved")) {
            return 0.0;
        }

        return Double.parseDouble(rawData);
    }
}
