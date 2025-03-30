package com.abha.enms.leadmanager.models;

import com.abha.enms.shared.models.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tbl_contact")
public class Contact extends BaseEntity {
  @Column(nullable = false)
  private String name;
  private String title;
  private String url;
  private boolean isPrimary;
  @ManyToOne(targetEntity = Lead.class)
  @JoinColumn(name = "lead_id", referencedColumnName = "id", nullable = false)
  private Lead lead;
  @OneToMany(mappedBy = "contact", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<ContactDetails> contactDetails;
}
