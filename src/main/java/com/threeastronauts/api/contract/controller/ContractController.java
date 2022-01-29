package com.threeastronauts.api.contract.controller;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.service.ContractService;
import com.threeastronauts.api.contract.service.InvoiceService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contract-api")
public class ContractController {

  @Autowired
  ContractService contractService;

  @Autowired
  InvoiceService invoiceService;

  @PostMapping("/contracts")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> postNewContract(@RequestBody @Valid ContractPostRequest request) {
    return contractService.setUpNewContract(request);
  }

  @GetMapping("/contracts")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ContractDto> getContractById(@RequestParam Long vendorId,
      @RequestParam Long contractId) {

    return ResponseEntity.ok(contractService.getContract(contractId));
  }

  @PostMapping("/invoices")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> postNewInvoice(
      @RequestBody @Valid InvoicePostRequest invoicePostRequest) {

    invoiceService.createNewInvoice(invoicePostRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
