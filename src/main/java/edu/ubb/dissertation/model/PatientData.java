package edu.ubb.dissertation.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PatientData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "patientData",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PatientMeasurement> measurements = new ArrayList<>();

    public PatientData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PatientMeasurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<PatientMeasurement> measurements) {
        this.measurements = measurements;
    }
}
