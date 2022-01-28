package com.threeastronauts.api.contract.repository;

import com.threeastronauts.api.contract.model.Vendor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

  Optional<Vendor> findByUsername(String username);
}
