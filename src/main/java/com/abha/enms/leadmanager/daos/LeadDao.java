package com.abha.enms.leadmanager.daos;

import com.abha.enms.leadmanager.models.Lead;
import java.util.List;

public interface LeadDao {
  Lead saveLead(Lead lead);

  Long fetchDuplicateLeadByContactDetailsIn(List<String> contactValues);

  void saveAllLead(List<Lead> leadList);
}
