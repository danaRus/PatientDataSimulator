package edu.ubb.dissertation.controller;

import edu.ubb.dissertation.controller.request.GenerateDataRequest;
import edu.ubb.dissertation.service.PatientMeasurementSimulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientDataSimulatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDataSimulatorController.class);

    @Autowired
    private PatientMeasurementSimulatorService patientMeasurementSimulatorService;

    @PostMapping("/generateMeasurements")
    public void generatePatientMeasurements(@RequestBody GenerateDataRequest request) {
        try {
            LOGGER.info(String.format("Generating %d measurements", request.getMeasurementsNumber()));
            patientMeasurementSimulatorService.generateData(request.getMeasurementsNumber());
        } catch (Exception e) {
            LOGGER.error(String.format("Could not generate patient measurements. Exception: %s.", e.getMessage()));
        }
    }

}
