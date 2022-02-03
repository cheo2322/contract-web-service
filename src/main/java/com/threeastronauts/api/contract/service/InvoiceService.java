package com.threeastronauts.api.contract.service;

import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.exception.ContractValueExceedException;
import com.threeastronauts.api.contract.exception.ResourceNotFoundException;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Invoice;
import com.threeastronauts.api.contract.model.Invoice.Status;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.InvoiceRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class InvoiceService {

  @Autowired
  ContractRepository contractRepository;

  @Autowired
  InvoiceRepository invoiceRepository;

  public void createNewInvoice(InvoicePostRequest invoicePostRequest) {
    contractRepository.findById(invoicePostRequest.getContract().getId())
        .map(contract -> {
          Double totalInvoices = invoiceRepository
              .sumOfTotalInvoicesByVendorIdAndContractId(contract)
              .orElse(0.0);
          Double currentInvoice = invoicePostRequest.getInvoice().getTotal();

          if (totalInvoices + currentInvoice <= contract.getValue()) {
            Invoice invoice = Invoice.builder()
                .description(createInvoiceDescription(contract))
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .status(Status.APPROVED)
                .contract(contract)
                .timeInHours(invoicePostRequest.getInvoice().getTimeInHours())
                .hourCost(invoicePostRequest.getInvoice().getHourCost())
                .otherMaterials(invoicePostRequest.getInvoice().getOtherMaterials())
                .otherMaterialsCost(invoicePostRequest.getInvoice().getOtherMaterialsCost())
                .total(invoicePostRequest.getInvoice().getTotal())
                .build();

            contract.getInvoices().add(invoice);

            contractRepository.save(contract);
            invoiceRepository.save(invoice);

          } else {
            throw new ContractValueExceedException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED,
                totalInvoices + currentInvoice - contract.getValue());
          }

          return contract;
        })
        .orElseThrow(() -> {
          throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Contract not found!");
        });
  }

  public InvoiceDto getInvoice(Long invoiceId) {
    return invoiceRepository.findById(invoiceId)
        .map(invoice -> InvoiceDto.builder()
            .description(invoice.getDescription())
            .status(invoice.getStatus())
            .timeInHours(invoice.getTimeInHours())
            .hourCost(invoice.getHourCost())
            .otherMaterials(invoice.getOtherMaterials())
            .otherMaterialsCost(invoice.getOtherMaterialsCost())
            .total(invoice.getTotal())
            .build())
        .orElseThrow(() -> {
          throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Invoice not found!");
        });
  }

  private String createInvoiceDescription(Contract contract) {
    return "Contract value: " + contract.getValue();
  }
}
