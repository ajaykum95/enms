package com.abha.enms.shared.models;

import com.abha.sharedlibrary.shared.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Base entity class that provides common fields for database entities.
 * This class includes fields for ID, timestamps for creation and updates,
 * and tracking fields for created and updated users.
 * It is annotated with {@code @MappedSuperclass}, meaning its fields are inherited by subclasses.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Date createdAt;

  @UpdateTimestamp
  private Date updatedAt;

  @Column(nullable = false, updatable = false)
  private String createdBy;

  private String updatedBy;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;
}
