package com.threeastronauts.api.contract.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "contracts")
public class Contract {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "contract_id")
  private long id;

  private int approved;

  @Lob
  @Column(columnDefinition = "BLOB")
  private String terms;
}
