package com.abha.enms.leadmanager.repositories;

import com.abha.enms.leadmanager.models.Lead;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadRepository extends JpaRepository<Lead, Long> {
  @Query(value = "SELECT DISTINCT l.id FROM tbl_lead l " +
      "JOIN tbl_contact c ON c.lead_id = l.id " +
      "JOIN tbl_contact_details cd ON cd.contact_id = c.id " +
      "WHERE l.status != 'DELETED' " +
      "AND l.duplicate_of IS NULL " +
      "AND cd.status != 'DELETED' " +
      "AND cd.value IN (:contactValues) " +
      "LIMIT 1",
      nativeQuery = true)
  Optional<Long> fetchDuplicateLeadByContactDetailsIn(
      @Param("contactValues") List<String> contactValues);
}
