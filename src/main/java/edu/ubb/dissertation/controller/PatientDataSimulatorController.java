package edu.ubb.dissertation.controller;

import edu.ubb.dissertation.controller.request.GenerateDataRequest;
import edu.ubb.dissertation.service.PatientMeasurementSimulatorService;
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
    public ResponseEntity<String> generatePatientMeasurements(@RequestBody GenerateDataRequest request) {
        try {
            patientMeasurementSimulatorService.generateData(request.getMeasurementsNumber());
            LOGGER.info(String.format("Generated %d measurements.", request.getMeasurementsNumber()));
            return ResponseEntity.ok(String.format("%d patient measurements generated.", request.getMeasurementsNumber()));
        } catch (Exception e) {
            LOGGER.error(String.format("Could not generate patient measurements. Exception: %s.", e.getMessage()));
            return ResponseEntity.badRequest().body("An error occurred while generating patient measurements.");
        }
    }

}
