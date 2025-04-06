package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "tbl_lead")
public class Lead extends BaseEntity {
  @Column(nullable = false)
  private String companyName;
  private String url;
  @Column(columnDefinition = "TEXT")
  private String companyDesc;
  @Column(nullable = false)
  private String source;
  private Long duplicateOf;
  @Column(nullable = false)
  private String leadStatus;
  @Column(nullable = false)
  private Long subscriberId;
  @OneToMany(mappedBy = "lead", cascade = {CascadeType.MERGE, CascadeType.PERSIST},
      fetch = FetchType.EAGER)
  private List<Contact> contacts;
  @OneToMany(mappedBy = "lead", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<LeadAddress> leadAddresses;
  @OneToMany(mappedBy = "lead", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<CustomFields> customFields;
}
