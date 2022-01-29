package com.threeastronauts.api.contract.service;

import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.model.Contract;
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
        .map(vendor ->
            contractRepository.findById(invoicePostRequest.getContract().getId())
                .map(contract -> {
                  Double totalInvoices = invoiceRepository
                      .sumOfTotalInvoicesByVendorIdAndContractId(vendor, contract)
                      .orElse(0.0);

                  Double currentInvoice = invoicePostRequest.getInvoice().getTotal();

                  if (totalInvoices + currentInvoice <= contract.getValue()) {

                    Invoice invoice = Invoice.builder()
                        .description(createInvoiceDescription(contract))
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
                    contract.getInvoices().add(invoice);

                    vendorRepository.save(vendor);
                    contractRepository.save(contract);
                    invoiceRepository.save(invoice);
                  } else {
                    log.error("Invoices exceed the contract value by {} dollars!",
                        totalInvoices + currentInvoice - contract.getValue());
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                  }

                  return contract;
                })
                .orElseThrow(() -> {
                      log.error("error contract!");
                      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                    }
                )
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

  private String createInvoiceDescription(Contract contract) {
    return "Contract value: " + contract.getValue();
  }
}
