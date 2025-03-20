package com.abha.enms.leadmanager.repositories;

import com.abha.enms.leadmanager.models.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
