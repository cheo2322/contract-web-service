package com.threeastronauts.api.contract.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ContractServiceTest {

  @Autowired
  ContractService contractService;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractRepository contractRepository;

  @Test
  void shouldReturnOkWhenANewContractIsCreated() {
    Client client = ContractTestHelper.createClient();
    Vendor vendor = ContractTestHelper.createVendor();
    ContractPostRequest contractPostRequest = ContractTestHelper.createContractPostRequest();

    clientRepository.save(client);
    vendorRepository.save(vendor);

    ResponseEntity<Void> response = contractService.setUpNewContract(contractPostRequest);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
  }

  @Test
  void shouldReturnContractInformation() {
    Client client = ContractTestHelper.createClient();
    Vendor vendor = ContractTestHelper.createVendor();
    ContractPostRequest contractPostRequest = ContractTestHelper.createContractPostRequest();

    clientRepository.save(client);
    vendorRepository.save(vendor);

    contractService.setUpNewContract(contractPostRequest);

    ContractDto contractDto = contractService.getContract(1L);

    assertThat(contractDto.getTerms(), equalTo("Terms."));
  }
}