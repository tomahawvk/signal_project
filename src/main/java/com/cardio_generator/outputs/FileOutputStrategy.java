package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// Google Java Style: Class names use UpperCamelCase and must match the file name.
/**
 * Writes generated patient data to files grouped by measurement label.
 *
 * <p>Each label is mapped to a text file under the configured base directory.
 * New output is appended so repeated simulator runs can preserve earlier data.
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;

    // Google Java Style: Variable and field names use lowerCamelCase, not underscores.
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Creates a file output strategy for the specified base directory.
     *
     * @param baseDirectory the directory where label-specific output files are stored
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Appends one generated data event to the file associated with its label.
     *
     * @param patientId the identifier of the patient associated with the data
     * @param timestamp the event time in milliseconds since the Unix epoch
     * @param label the measurement or event type used to select the output file
     * @param data the generated value or status text to write
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Google Java Style: Local variable names use lowerCamelCase. Long method calls are wrapped to keep lines readable.
        String filePath =
                fileMap.computeIfAbsent(
                        label, key -> Paths.get(baseDirectory, label + ".txt").toString());
        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
