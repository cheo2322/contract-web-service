package com.threeastronauts.api.contract.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "invoices")
public class Invoice {

  public enum Status {
    IN_PROGRESS,
    SUBMITTED,
    APPROVED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "invoice_id")
  private long id;

  private String description;
  private LocalDateTime creationDate;
  private Status status;

  @Column(name = "time_in_hours")
  private double timeInHours;

  @Column(name = "hour_cost")
  private double hourCost;

  @Column(name = "other_materials")
  private String otherMaterials;

  @Column(name = "other_materials_cost")
  private double otherMaterialsCost;

  private double total;

  @ManyToOne
  @JoinColumn(name = "contract_id")
  private Contract contract;
}
