package edu.ubb.dissertation.repository;

import edu.ubb.dissertation.model.Surgery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurgeryRepository extends CrudRepository<Surgery, String> {
}
