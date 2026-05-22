package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Starts reading data from a streaming source.
     *
     * <p>Batch readers can rely on the default implementation, which delegates to
     * {@link #readData(DataStorage)}.
     *
     * @param dataStorage the storage where incoming data will be stored
     * @throws IOException if the streaming source cannot be started
     */
    default void start(DataStorage dataStorage) throws IOException {
        readData(dataStorage);
    }

    /**
     * Stops reading from a streaming source.
     *
     * @throws IOException if the streaming source cannot be stopped
     */
    default void stop() throws IOException {
        // Default no-op for non-streaming readers.
    }
}
