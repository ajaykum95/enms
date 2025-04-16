package com.abha.enms.leadmanager.daos;

import com.abha.enms.leadmanager.models.Lead;
import com.abha.sharedlibrary.shared.enums.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadDao {
    Lead saveLead(Lead lead);

    Long fetchDuplicateLeadByContactDetailsIn(List<String> contactValues);

    void saveAllLead(List<Lead> leadList);

    Page<Lead> getAllLeadsBySubscriberIdAndStatusNot(
            Long subscriberId, Status status, Pageable pageable);

    Optional<Lead> findByIdAndSubscriberIdAndStatusNot(
            Long id, Long subscriberId, Status status);
}
