package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseDbEntity;
import com.abha.sharedlibrary.enms.enums.ContactType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ContactDetails extends BaseDbEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ContactType contactType;

  @Column(nullable = false)
  private String value;

  @ManyToOne(targetEntity = Contact.class)
  @JoinColumn(name = "contact_id", referencedColumnName = "id")
  private Contact contact;
}
