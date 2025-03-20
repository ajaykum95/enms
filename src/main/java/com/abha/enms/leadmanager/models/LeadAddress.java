package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseEntity;
import com.abha.sharedlibrary.shared.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tbl_lead_address")
public class LeadAddress extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AddressType addressType;

  private boolean isDefault;

  @Column(nullable = false)
  private String addressLine1;

  private String addressLine2;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String state;

  @Column(nullable = false)
  private String zipcode;

  @Column(nullable = false)
  private String country;

  @ManyToOne(targetEntity = Lead.class)
  @JoinColumn(name = "lead_id", referencedColumnName = "id")
  private Lead lead;
}
