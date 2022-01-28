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
import lombok.Setter;

@Getter
@Entity
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private long id;

  private String username;

  @OneToMany(targetEntity = Contract.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "client_contract_fk", referencedColumnName = "client_id")
  private List<Contract> contracts;
}
