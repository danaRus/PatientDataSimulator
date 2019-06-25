package edu.ubb.dissertation.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PatientMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientData patientData;

    private LocalDateTime timestamp;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "systolicBloodPressure")),
            @AttributeOverride(name = "upperLimit", column = @Column(name = "systolicBloodPressureUpperLimit")),
            @AttributeOverride(name = "lowerLimit", column = @Column(name = "systolicBloodPressureLowerLimit"))
    })
    private Measurement systolicBloodPressure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "diastolicBloodPressure")),
            @AttributeOverride(name = "upperLimit", column = @Column(name = "diastolicBloodPressureUpperLimit")),
            @AttributeOverride(name = "lowerLimit", column = @Column(name = "diastolicBloodPressureLowerLimit"))
    })
    private Measurement diastolicBloodPressure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "heartRate")),
            @AttributeOverride(name = "upperLimit", column = @Column(name = "heartRateUpperLimit")),
            @AttributeOverride(name = "lowerLimit", column = @Column(name = "heartRateLowerLimit"))
    })
    private Measurement heartRate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "oxygenSaturationLevel")),
            @AttributeOverride(name = "upperLimit", column = @Column(name = "oxygenSaturationLevelUpperLimit")),
            @AttributeOverride(name = "lowerLimit", column = @Column(name = "oxygenSaturationLevelLowerLimit"))
    })
    private Measurement oxygenSaturationLevel;

    private Double bloodLossRate;

    public PatientMeasurement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public PatientData getPatientData() {
        return patientData;
    }

    public void setPatientData(final PatientData patientData) {
        this.patientData = patientData;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Measurement getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(final Measurement systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public Measurement getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(final Measurement diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public Measurement getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(final Measurement heartRate) {
        this.heartRate = heartRate;
    }

    public Measurement getOxygenSaturationLevel() {
        return oxygenSaturationLevel;
    }

    public void setOxygenSaturationLevel(final Measurement oxygenSaturationLevel) {
        this.oxygenSaturationLevel = oxygenSaturationLevel;
    }

    public Double getBloodLossRate() {
        return bloodLossRate;
    }

    public void setBloodLossRate(final Double bloodLossRate) {
        this.bloodLossRate = bloodLossRate;
    }
}
