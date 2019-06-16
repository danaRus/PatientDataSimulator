package edu.ubb.dissertation.service;

import edu.ubb.dissertation.model.*;
import edu.ubb.dissertation.repository.AbnormalVitalSignRepository;
import edu.ubb.dissertation.repository.PatientDataRepository;
import edu.ubb.dissertation.repository.PatientMeasurementRepository;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static edu.ubb.dissertation.model.AbnormalVitalSignType.*;
import static edu.ubb.dissertation.util.Constants.*;

@Service
public class PatientMeasurementSimulatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientMeasurementSimulatorService.class);

    private static final double ERROR_RANGE = 5.0;

    @Autowired
    private PatientMeasurementRepository patientMeasurementRepository;

    @Autowired
    private AbnormalVitalSignRepository abnormalVitalSignRepository;

    @Autowired
    private PatientDataRepository patientDataRepository;

    public void generateData(final Integer measurementsNumber) {
        Optional.ofNullable(measurementsNumber)
                .ifPresent(this::generateMeasurements);
    }

    private void generateMeasurements(final Integer measurementsNumber) {
        final PatientData patientData = retrieveOrCreatePatient();

        IntStream.range(0, measurementsNumber)
                .mapToObj(i -> patientMeasurementRepository.save(createPatientMeasurement(patientData)))
                .peek(measurement -> {
                    try {
                        // needed in order to generate the data only at a certain time interval (half a minute)
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        LOGGER.error(String.format("Exception occurred when calling sleep: %s", e.getMessage()));
                    }
                })
                .forEach(patientMeasurement -> abnormalVitalSignRepository.save(createAbnormalVitalSignsEntry(patientMeasurement)));
    }

    private PatientMeasurement createPatientMeasurement(final PatientData patientData) {
        final PatientMeasurement patientMeasurement = new PatientMeasurement();
        patientMeasurement.setPatientData(patientData);
        patientMeasurement.setTimestamp(LocalDateTime.now());
        patientMeasurement.setSystolicBloodPressure(
                createMeasurement(SYSTOLIC_BLOOD_PRESSURE_UPPER_LIMIT, SYSTOLIC_BLOOD_PRESSURE_LOWER_LIMIT));
        patientMeasurement.setDiastolicBloodPressure(
                createMeasurement(DIASTOLIC_BLOOD_PRESSURE_UPPER_LIMIT, DIASTOLIC_BLOOD_PRESSURE_LOWER_LIMIT));
        patientMeasurement.setHeartRate(
                createMeasurement(HEART_RATE_UPPER_LIMIT, HEART_RATE_LOWER_LIMIT));
        patientMeasurement.setOxygenSaturationLevel(
                createMeasurement(OXYGEN_SATURATION_LEVEL_UPPER_LIMIT, OXYGEN_SATURATION_LEVEL_LOWER_LIMIT));
        patientMeasurement.setBloodLossRate(
                generateRandomValue(BLOOD_LOSS_LEVEL_LOWER_LIMIT, BLOOD_LOSS_LEVEL_UPPER_LIMIT));
        return patientMeasurement;
    }

    private PatientData retrieveOrCreatePatient() {
        return patientDataRepository.count() == 0
                ? patientDataRepository.save(new PatientData())
                : patientDataRepository.findAll().iterator().next();
    }

    private Measurement createMeasurement(final double upperLimit, final double lowerLimit) {
        final Measurement measurement = new Measurement();
        measurement.setValue(generateRandomValue(lowerLimit - ERROR_RANGE, upperLimit + ERROR_RANGE));
        measurement.setUpperLimit(upperLimit);
        measurement.setLowerLimit(lowerLimit);
        return measurement;
    }

    private AbnormalVitalSignsEntry createAbnormalVitalSignsEntry(final PatientMeasurement patientMeasurement) {
        final AbnormalVitalSignsEntry abnormalVitalSignsEntry = new AbnormalVitalSignsEntry();
        abnormalVitalSignsEntry.setPatientMeasurement(patientMeasurement);
        abnormalVitalSignsEntry.setAbnormalVitalSignTypes(extractAbnormalVitalSignTypes(patientMeasurement));
        return abnormalVitalSignsEntry;
    }

    private Set<AbnormalVitalSignType> extractAbnormalVitalSignTypes(final PatientMeasurement patientMeasurement) {
        final Set<AbnormalVitalSignType> abnormalVitalSignTypes = new HashSet<>();

        addAbnormalVitalSignsForMeasurement(patientMeasurement.getSystolicBloodPressure(),
                abnormalVitalSignTypes, SYSTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT, SYSTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getSystolicBloodPressure(),
                abnormalVitalSignTypes, DIASTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT, DIASTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getHeartRate(),
                abnormalVitalSignTypes, HEART_RATE_BELOW_LOWER_LIMIT, HEART_RATE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getOxygenSaturationLevel(),
                abnormalVitalSignTypes, OXYGEN_SATURATION_LEVEL_BELOW_LOWER_LIMIT, OXYGEN_SATURATION_LEVEL_ABOVE_UPPER_LIMIT);

        return abnormalVitalSignTypes;
    }

    private void addAbnormalVitalSignsForMeasurement(final Measurement measurement, final Set<AbnormalVitalSignType> abnormalVitalSignTypes,
                                                     final AbnormalVitalSignType below, final AbnormalVitalSignType above) {
        if (measurement.getValue() < measurement.getLowerLimit()) {
            abnormalVitalSignTypes.add(below);
        }
        if (measurement.getValue() > measurement.getUpperLimit()) {
            abnormalVitalSignTypes.add(above);
        }
    }

    private Double generateRandomValue(final double lowerBound, final Double upperBound) {
        return roundValue(ThreadLocalRandom.current().nextDouble(lowerBound, upperBound));
    }

    private Double roundValue(final double value) {
        return Precision.round(value, 2);
    }

}
