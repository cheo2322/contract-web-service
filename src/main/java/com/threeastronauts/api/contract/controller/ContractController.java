package com.threeastronauts.api.contract.controller;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.service.ContractService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ContractController {

  @Autowired
  ContractService contractService;

  @PostMapping("/contracts")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> postNewContract(@RequestBody @Valid ContractPostRequest request) {
    return contractService.setUpNewContract(request);
  }
}
