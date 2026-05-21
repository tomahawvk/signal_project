package com.cardio_generator.outputs;

/**
 * Defines the contract for publishing generated health data.
 *
 * <p>Implementations can write measurements to the console, files, sockets, or
 * other output targets while keeping generators independent from the destination.
 */
public interface OutputStrategy {

    /**
     * Publishes one generated patient measurement or status event.
     *
     * @param patientId the identifier of the patient associated with the data
     * @param timestamp the event time in milliseconds since the Unix epoch
     * @param label the measurement or event type, such as {@code Saturation}
     * @param data the generated value or status text to output
     */
    void output(int patientId, long timestamp, String label, String data);
}
