package com.alerts;

/**
 * Defines the common behavior of all alerts in the monitoring system.
 */
public interface Alert {
    String getPatientId();

    String getCondition();

    long getTimestamp();

    String getPriority();

    boolean shouldRepeat();
}
