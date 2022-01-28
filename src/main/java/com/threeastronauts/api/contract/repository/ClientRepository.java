package com.threeastronauts.api.contract.repository;

import com.threeastronauts.api.contract.model.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  Optional<Client> findByUsername(String username);
}
