package edu.ubb.dissertation.repository;

import edu.ubb.dissertation.model.PatientMeasurement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMeasurementRepository extends CrudRepository<PatientMeasurement, Long> {
}
