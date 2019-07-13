package edu.ubb.dissertation.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Surgery {

    @Id
    private String id;

    @OneToMany(
            mappedBy = "surgery",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PatientMeasurement> measurements = new ArrayList<>();

    public Surgery() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<PatientMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(final List<PatientMeasurement> measurements) {
        this.measurements = measurements;
    }
}
