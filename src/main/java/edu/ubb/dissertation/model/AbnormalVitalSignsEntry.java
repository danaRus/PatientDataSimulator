package edu.ubb.dissertation.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AbnormalVitalSignsEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private PatientMeasurement patientMeasurement;

    @ElementCollection
    private Set<AbnormalVitalSignType> abnormalVitalSignTypes;

    public AbnormalVitalSignsEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public PatientMeasurement getPatientMeasurement() {
        return patientMeasurement;
    }

    public void setPatientMeasurement(final PatientMeasurement patientMeasurement) {
        this.patientMeasurement = patientMeasurement;
    }

    public Set<AbnormalVitalSignType> getAbnormalVitalSignTypes() {
        return abnormalVitalSignTypes;
    }

    public void setAbnormalVitalSignTypes(final Set<AbnormalVitalSignType> abnormalVitalSignTypes) {
        this.abnormalVitalSignTypes = abnormalVitalSignTypes;
    }
}
