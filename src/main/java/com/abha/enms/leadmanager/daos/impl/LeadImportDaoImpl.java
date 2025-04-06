package com.abha.enms.leadmanager.daos.impl;

import com.abha.enms.leadmanager.daos.LeadImportDao;
import com.abha.enms.leadmanager.models.LeadImportHistory;
import com.abha.enms.leadmanager.repositories.LeadImportRepo;
import com.abha.sharedlibrary.shared.enums.Status;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LeadImportDaoImpl implements LeadImportDao {

    private final LeadImportRepo leadImportRepo;

    public LeadImportDaoImpl(LeadImportRepo leadImportRepo) {
        this.leadImportRepo = leadImportRepo;
    }

    @Override
    public void saveLeadImport(LeadImportHistory leadImportHistory) {
        leadImportRepo.save(leadImportHistory);
    }

    @Override
    public List<LeadImportHistory> getAllLeadImportHistory(long subscriberId) {
        return leadImportRepo.findBySubscriberIdAndStatus(subscriberId, Status.ACTIVE);
    }
}
