package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Defines the contract for components that generate simulated patient data.
 *
 * <p>Implementations generate one type of measurement for a patient and pass
 * the result to an {@link OutputStrategy}.
 */
public interface PatientDataGenerator {

    /**
     * Generates one measurement or status update for the specified patient.
     *
     * @param patientId the identifier of the patient whose data is generated
     * @param outputStrategy the destination used to publish generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
