package edu.ubb.dissertation.model;

public enum AbnormalVitalSignType {

    SYSTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT("SYSTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT"),
    SYSTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT("SYSTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT"),
    DIASTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT("DIASTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT"),
    DIASTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT("DIASTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT"),
    HEART_RATE_BELOW_LOWER_LIMIT("HEART_RATE_BELOW_LOWER_LIMIT"),
    HEART_RATE_ABOVE_UPPER_LIMIT("HEART_RATE_ABOVE_UPPER_LIMIT"),
    OXYGEN_SATURATION_LEVEL_BELOW_LOWER_LIMIT("OXYGEN_SATURATION_LEVEL_BELOW_LOWER_LIMIT"),
    OXYGEN_SATURATION_LEVEL_ABOVE_UPPER_LIMIT("OXYGEN_SATURATION_LEVEL_ABOVE_UPPER_LIMIT");

    private String code;

    AbnormalVitalSignType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
