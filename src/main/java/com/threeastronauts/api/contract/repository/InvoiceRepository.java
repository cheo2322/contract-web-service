package com.threeastronauts.api.contract.repository;

import com.threeastronauts.api.contract.model.Invoice;
import com.threeastronauts.api.contract.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

  @Query("SELECT COUNT(i) FROM Invoice i WHERE i.approved=1 AND i.vendor=?1")
  Long countApprovedInvoicesByVendorId(Vendor vendor);
}
