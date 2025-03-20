package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseEntity;
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
@Table(name = "tbl_custom_fields")
public class CustomFields extends BaseEntity {

  @Column(nullable = false)
  private String fieldName;

  @Column(nullable = false)
  private String fieldValue;

  @ManyToOne(targetEntity = Lead.class)
  @JoinColumn(name = "lead_id", referencedColumnName = "id")
  private Lead lead;
}
