package com.threeastronauts.api.contract.repository;

import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Invoice;
import com.threeastronauts.api.contract.model.Vendor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

  @Query("SELECT SUM(total) FROM Invoice i WHERE i.vendor=?1 AND i.contract=?2")
  Optional<Double> sumOfTotalInvoicesByVendorIdAndContractId(Vendor vendor, Contract contract);
}
