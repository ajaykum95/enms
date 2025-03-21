package com.abha.enms.leadmanager.daos.impl;

import com.abha.enms.leadmanager.daos.LeadDao;
import com.abha.enms.leadmanager.models.Lead;
import com.abha.enms.leadmanager.repositories.LeadRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LeadDaoImpl implements LeadDao {

  private final LeadRepository leadRepository;

  public LeadDaoImpl(LeadRepository leadRepository) {
    this.leadRepository = leadRepository;
  }

  @Override
  public Lead saveLead(Lead lead) {
    return leadRepository.save(lead);
  }

  @Override
  public Long fetchDuplicateLeadByContactDetailsIn(List<String> contactValues) {
    return leadRepository.fetchDuplicateLeadByContactDetailsIn(contactValues)
        .orElse(null);
  }

  @Override
  public void saveAllLead(List<Lead> leadList) {
    leadRepository.saveAll(leadList);
  }
}
