package edu.ubb.dissertation.model;

import javax.persistence.Embeddable;

@Embeddable
public class Measurement {

    private Double value;

    private Double upperLimit;

    private Double lowerLimit;

    public Measurement() {
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public Double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(final Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(final Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
}
