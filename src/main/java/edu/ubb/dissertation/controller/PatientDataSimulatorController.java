package edu.ubb.dissertation.controller;

import edu.ubb.dissertation.controller.request.GenerateDataRequest;
import edu.ubb.dissertation.service.PatientMeasurementSimulatorService;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientDataSimulatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDataSimulatorController.class);

    @Autowired
    private PatientMeasurementSimulatorService patientMeasurementSimulatorService;

    @PostMapping("/generateMeasurements")
    public ResponseEntity<String> generatePatientMeasurements(final @RequestBody GenerateDataRequest request) {
        return Try.of(() -> generateData(request.getMeasurementsNumber()))
                .onFailure(e -> LOGGER.error("Could not generate patient measurements. Message: {}.", e.getMessage()))
                .getOrElseGet(e -> ResponseEntity.badRequest().body("An error occurred while generating patient measurements."));
    }

    private ResponseEntity<String> generateData(final Integer measurementsNumber) {
        patientMeasurementSimulatorService.generateData(measurementsNumber);
        LOGGER.info(String.format("Generated %d measurements.", measurementsNumber));
        return ResponseEntity.ok(String.format("%d patient measurements generated.", measurementsNumber));
    }
}
