package com.threeastronauts.api.contract.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "clients")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "client_id")
  private long id;

  private String username;

  @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
  private List<Contract> contracts;
}
