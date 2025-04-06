package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseDbEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "tbl_lead_import_detail")
public class LeadImportDetails extends BaseDbEntity {

    @Column(nullable = false)
    private Long leadId;

    @ManyToOne(targetEntity = LeadImportHistory.class)
    @JoinColumn(name = "lead_import_id", referencedColumnName = "id", nullable = false)
    private LeadImportHistory leadImportHistory;
}
