package com.abha.enms.leadmanager.repositories;

import com.abha.enms.leadmanager.models.LeadImportHistory;
import com.abha.sharedlibrary.shared.enums.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadImportRepo extends JpaRepository<LeadImportHistory, Long> {
    List<LeadImportHistory> findBySubscriberIdAndStatus(long subscriberId, Status status);
}
