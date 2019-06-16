package edu.ubb.dissertation.repository;

import edu.ubb.dissertation.model.PatientData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDataRepository extends CrudRepository<PatientData, Long> {
}
