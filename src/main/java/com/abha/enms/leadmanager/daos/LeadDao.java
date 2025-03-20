package com.abha.enms.leadmanager.daos;

import com.abha.enms.leadmanager.models.Lead;

public interface LeadDao {
  Lead saveLead(Lead lead);
}
