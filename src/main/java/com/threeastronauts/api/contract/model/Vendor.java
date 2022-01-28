package com.threeastronauts.api.contract.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "vendors")
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "vendor_id")
  private long id;

  private String username;

  @OneToMany(targetEntity = Contract.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "vendors_contract_fk", referencedColumnName = "vendor_id")
  private List<Contract> contracts;
}
