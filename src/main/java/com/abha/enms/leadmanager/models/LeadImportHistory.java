package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseEntity;
import com.abha.sharedlibrary.shared.enums.OperationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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
@Table(name = "tbl_lead_import_history")
public class LeadImportHistory extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus operationStatus;

    @Column(nullable = false)
    private String fileName;

    private int failed;

    private int success;

    @Column(nullable = false)
    private Long subscriberId;

    @OneToMany(mappedBy = "leadImportHistory", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<LeadImportDetails> leadImportDetails;
}
