package data_management;

import static org.junit.jupiter.api.Assertions.assertSame;

import com.cardio_generator.HealthDataSimulator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

class SingletonTest {

    @Test
    void dataStorageReturnsSameInstance() {
        DataStorage first = DataStorage.getInstance();
        DataStorage second = DataStorage.getInstance();

        assertSame(first, second);
    }

    @Test
    void healthDataSimulatorReturnsSameInstance() {
        HealthDataSimulator first = HealthDataSimulator.getInstance();
        HealthDataSimulator second = HealthDataSimulator.getInstance();

        assertSame(first, second);
    }
}