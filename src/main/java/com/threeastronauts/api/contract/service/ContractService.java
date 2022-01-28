package com.threeastronauts.api.contract.service;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.mapper.ContractMapper;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContractService {

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractMapper contractMapper;

  public ResponseEntity<Void> setUpNewContract(ContractPostRequest contractPostRequest) {
    clientRepository.findByUsername(contractPostRequest.getClient().getUsername())
        .map(client -> {
          vendorRepository.findByUsername(contractPostRequest.getVendor().getUsername())
              .map(vendor -> {
                Contract contract = contractMapper
                    .contractDtoToContract(contractPostRequest.getContract());

                client.getContracts().add(contract);
                vendor.getContracts().add(contract);

                return vendor;
              })
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

          return client;
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return ResponseEntity.ok().build();
  }
}
