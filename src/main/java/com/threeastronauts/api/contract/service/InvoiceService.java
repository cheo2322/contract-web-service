package com.threeastronauts.api.contract.service;

import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.model.Invoice;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.InvoiceRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
public class InvoiceService {

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractRepository contractRepository;

  @Autowired
  InvoiceRepository invoiceRepository;

  public void createNewInvoice(InvoicePostRequest invoicePostRequest) {
    vendorRepository.findByUsername(invoicePostRequest.getVendor().getUsername())
        .map(vendor -> {
              if (invoiceRepository.countApprovedInvoicesByVendorId(vendor) <= 0) {
                return contractRepository.findById(invoicePostRequest.getContract().getId())
                    .map(contract -> {
                      Invoice invoice = Invoice.builder()
                          .approved(1)
                          .contract(contract)
                          .vendor(vendor)
                          .timeInHours(invoicePostRequest.getInvoice().getTimeInHours())
                          .hourCost(invoicePostRequest.getInvoice().getHourCost())
                          .otherMaterials(invoicePostRequest.getInvoice().getOtherMaterials())
                          .otherMaterialsCost(invoicePostRequest.getInvoice().getOtherMaterialsCost())
                          .total(invoicePostRequest.getInvoice().getTotal())
                          .build();

                      vendor.getInvoices().add(invoice);
                      contract.setInvoice(invoice);

                      vendorRepository.save(vendor);
                      contractRepository.save(contract);
                      invoiceRepository.save(invoice);

                      return contract;
                    })
                    .orElseThrow(() -> {
                      log.error("error contract!");
                      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                    });
              } else {
                log.error("Vendor has already an approved invoice!");
                throw new ResponseStatusException(HttpStatus.CONFLICT);
              }
            }
        )
        .orElseThrow(() -> {
          log.error("error vendor!");
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
  }

  public InvoiceDto getInvoice(Long invoiceId) {
    return invoiceRepository.findById(invoiceId)
        .map(invoice -> InvoiceDto.builder()
            .timeInHours(invoice.getTimeInHours())
            .hourCost(invoice.getHourCost())
            .otherMaterials(invoice.getOtherMaterials())
            .otherMaterialsCost(invoice.getOtherMaterialsCost())
            .total(invoice.getTotal())
            .build())
        .orElseThrow(() -> {
          log.error("error vendor!");
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
  }
}