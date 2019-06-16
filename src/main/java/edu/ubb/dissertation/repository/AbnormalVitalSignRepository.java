package edu.ubb.dissertation.repository;

import edu.ubb.dissertation.model.AbnormalVitalSignsEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbnormalVitalSignRepository extends CrudRepository<AbnormalVitalSignsEntry, Long> {
}
