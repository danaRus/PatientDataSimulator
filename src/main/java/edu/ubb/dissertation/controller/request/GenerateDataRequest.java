package edu.ubb.dissertation.controller.request;

/**
 * Request used for generating patient data. The number of measurements was wrapped in a request in order to
 * easily accommodate its extension if needed.
 */
public class GenerateDataRequest {

    private Integer measurementsNumber;

    public Integer getMeasurementsNumber() {
        return measurementsNumber;
    }

    public void setMeasurementsNumber(final Integer measurementsNumber) {
        this.measurementsNumber = measurementsNumber;
    }
}
