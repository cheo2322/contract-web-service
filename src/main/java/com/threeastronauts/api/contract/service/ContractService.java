package com.threeastronauts.api.contract.service;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
public class ContractService {

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractRepository contractRepository;

  public ResponseEntity<Void> setUpNewContract(ContractPostRequest contractPostRequest) {
    clientRepository.findByUsername(contractPostRequest.getClient().getUsername())
        .map(client -> {
          vendorRepository.findByUsername(contractPostRequest.getVendor().getUsername())
              .map(vendor -> {
                Contract contract = Contract.builder()
                    .terms(contractPostRequest.getContract().getTerms())
                    .approved(1)
                    .client(client)
                    .vendor(vendor)
                    .build();

                client.getContracts().add(contract);
                vendor.getContracts().add(contract);

                clientRepository.save(client);
                vendorRepository.save(vendor);
                contractRepository.save(contract);

                return vendor;
              })
              .orElseThrow(() -> {
                log.error("error vendors!");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
              });

          return client;
        })
        .orElseThrow(() -> {
          log.error("error clients!");
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
