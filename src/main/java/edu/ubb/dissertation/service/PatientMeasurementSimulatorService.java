package edu.ubb.dissertation.service;

import edu.ubb.dissertation.model.*;
import edu.ubb.dissertation.mqtt.MqttClientConnector;
import edu.ubb.dissertation.repository.AbnormalVitalSignRepository;
import edu.ubb.dissertation.repository.PatientDataRepository;
import edu.ubb.dissertation.repository.PatientMeasurementRepository;
import edu.ubb.dissertation.repository.SurgeryRepository;
import io.vavr.control.Try;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static edu.ubb.dissertation.model.AbnormalVitalSignType.*;
import static edu.ubb.dissertation.util.Constants.*;
import static edu.ubb.dissertation.util.MqttMessageCreator.createMqttMessage;
import static java.time.ZoneOffset.UTC;

/**
 * Encapsulates the logic for generating all the entities needed for (associated to) a patient measurement.
 */
@Service
public class PatientMeasurementSimulatorService {

    private static final double ERROR_RANGE = 5.0;

    @Autowired
    private PatientMeasurementRepository patientMeasurementRepository;
    @Autowired
    private AbnormalVitalSignRepository abnormalVitalSignRepository;
    @Autowired
    private PatientDataRepository patientDataRepository;
    @Autowired
    private SurgeryRepository surgeryRepository;
    private MqttClientConnector mqttClientConnector;

    public PatientMeasurementSimulatorService() {
        mqttClientConnector = new MqttClientConnector();
    }

    public void generateData(final Integer measurementsNumber) {
        Optional.ofNullable(measurementsNumber)
                .ifPresent(this::generateMeasurements);
    }

    private void generateMeasurements(final Integer measurementsNumber) {
        final PatientData patientData = retrieveOrCreatePatient();
        final Surgery surgery = createSurgery();

        mqttClientConnector.connect();
        IntStream.range(0, measurementsNumber)
                .forEach(i -> generateEntry(patientData, surgery));
        mqttClientConnector.disconnect();
    }

    /*
     * Since the project was created in order to simulate data for a Proof-Of-Concept, the data is considered as
     * belonging to a single patient. As such, before creating the measurement, it must be ensured that a patient exists.
     */
    private PatientData retrieveOrCreatePatient() {
        return patientDataRepository.count() == 0
                ? patientDataRepository.save(new PatientData())
                : patientDataRepository.findAll().iterator().next();
    }

    private Surgery createSurgery() {
        final Surgery surgery = new Surgery();
        surgery.setId(UUID.randomUUID().toString());
        return surgeryRepository.save(surgery);
    }

    private void generateEntry(final PatientData patientData, final Surgery surgery) {
        final PatientMeasurement measurement = patientMeasurementRepository.save(createPatientMeasurement(patientData, surgery));
        final AbnormalVitalSignsEntry entry = abnormalVitalSignRepository.save(createAbnormalVitalSignsEntry(measurement));
        mqttClientConnector.publish(createMqttMessage(measurement, entry));
    }

    /*
     * For the Proof-Of-Concept application, the measurements must be generated every 10 seconds. This
     * is done in order to ensure the correlation with the other data used in the pipeline and as such make sure that
     * the processing/analytics performed on the data is relevant.
     */
    private PatientMeasurement createPatientMeasurement(final PatientData patientData, final Surgery surgery) {
        LocalDateTime timestamp = LocalDateTime.now(UTC).withNano(0);
        while (timestamp.getSecond() % 10 != 0) {
            timestamp = LocalDateTime.now(UTC).withNano(0);
        }
        // added in order to avoid having data generated with the same timestamp
        Try.run(() -> Thread.sleep(1000));
        return createPatientMeasurement(patientData, surgery, timestamp);
    }

    private PatientMeasurement createPatientMeasurement(final PatientData patientData, final Surgery surgery,
                                                        final LocalDateTime timestamp) {
        final PatientMeasurement patientMeasurement = new PatientMeasurement();
        patientMeasurement.setPatientData(patientData);
        patientMeasurement.setSurgery(surgery);
        patientMeasurement.setTimestamp(timestamp);
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
        abnormalVitalSignsEntry.setAbnormalVitalSigns(extractAbnormalVitalSigns(patientMeasurement));
        return abnormalVitalSignsEntry;
    }

    private Set<AbnormalVitalSignType> extractAbnormalVitalSigns(final PatientMeasurement patientMeasurement) {
        final Set<AbnormalVitalSignType> abnormalVitalSigns = new HashSet<>();

        addAbnormalVitalSignsForMeasurement(patientMeasurement.getSystolicBloodPressure(),
                abnormalVitalSigns, SYSTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT, SYSTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getSystolicBloodPressure(),
                abnormalVitalSigns, DIASTOLIC_BLOOD_PRESSURE_BELOW_LOWER_LIMIT, DIASTOLIC_BLOOD_PRESSURE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getHeartRate(),
                abnormalVitalSigns, HEART_RATE_BELOW_LOWER_LIMIT, HEART_RATE_ABOVE_UPPER_LIMIT);
        addAbnormalVitalSignsForMeasurement(patientMeasurement.getOxygenSaturationLevel(),
                abnormalVitalSigns, OXYGEN_SATURATION_LEVEL_BELOW_LOWER_LIMIT, OXYGEN_SATURATION_LEVEL_ABOVE_UPPER_LIMIT);

        return abnormalVitalSigns;
    }

    private void addAbnormalVitalSignsForMeasurement(final Measurement measurement, final Set<AbnormalVitalSignType> abnormalVitalSigns,
                                                     final AbnormalVitalSignType below, final AbnormalVitalSignType above) {
        if (measurement.getValue() < measurement.getLowerLimit()) {
            abnormalVitalSigns.add(below);
        }
        if (measurement.getValue() > measurement.getUpperLimit()) {
            abnormalVitalSigns.add(above);
        }
    }

    private Double generateRandomValue(final double lowerBound, final Double upperBound) {
        return roundValue(ThreadLocalRandom.current().nextDouble(lowerBound, upperBound));
    }

    private Double roundValue(final double value) {
        return Precision.round(value, 2);
    }
}
