package com.abha.enms.leadmanager.daos;

import com.abha.enms.leadmanager.models.LeadImportHistory;
import java.util.List;

public interface LeadImportDao {
    void saveLeadImport(LeadImportHistory leadImportHistory);

    List<LeadImportHistory> getAllLeadImportHistory(long subscriberId);
}
