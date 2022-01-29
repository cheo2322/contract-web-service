package com.threeastronauts.api.contract.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.dto.ClientDto;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.dto.VendorDto;
import com.threeastronauts.api.contract.exception.ResourceNotFoundException;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import org.junit.jupiter.api.BeforeAll;
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

  private static ContractPostRequest contractPostRequest;
  private static Client client;
  private static Vendor vendor;

  @BeforeAll
  static void setUp() {
    contractPostRequest = ContractTestHelper.createContractPostRequest();
    client = ContractTestHelper.createClient();
    vendor = ContractTestHelper.createVendor();
  }

  @Test
  void shouldReturnCreated() {
    client.setUsername("client-service");
    vendor.setUsername("vendor-username");

    contractPostRequest.setClient(ClientDto.builder()
        .username(client.getUsername())
        .build());

    contractPostRequest.setVendor(VendorDto.builder()
        .username(vendor.getUsername())
        .build());

    clientRepository.save(client);
    vendorRepository.save(vendor);

    ResponseEntity<Void> response = contractService.setUpNewContract(contractPostRequest);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
  }

  @Test
  void shouldReturnContractInformation() {
    client.setId(3L);
    client.setUsername("client-get-service");
    vendor.setId(3L);
    vendor.setUsername("vendor-get-username");

    contractPostRequest.setClient(ClientDto.builder()
        .username(client.getUsername())
        .build());

    contractPostRequest.setVendor(VendorDto.builder()
        .username(vendor.getUsername())
        .build());

    clientRepository.save(client);
    vendorRepository.save(vendor);

    contractService.setUpNewContract(contractPostRequest);

    ContractDto contractDto = contractService.getContract(1L);

    assertThat(contractDto.getTerms(), equalTo("Terms."));
  }

  @Test
  void shouldThrowsClientResourceNotFoundException() {
    contractPostRequest.getClient().setUsername("");

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> contractService.setUpNewContract(contractPostRequest));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    assertThat(exception.getReason(), equalTo("Client not found!"));
  }

  @Test
  void shouldThrowsVendorResourceNotFoundException() {
    Client client1 = Client.builder()
        .id(10L)
        .username("client0")
        .build();

    contractPostRequest.setClient(ClientDto.builder().username(client1.getUsername()).build());
    contractPostRequest.getVendor().setUsername("");

    clientRepository.save(client1);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> contractService.setUpNewContract(contractPostRequest));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    assertThat(exception.getReason(), equalTo("Vendor not found!"));
  }

  @Test
  void shouldReturnContractResourceNotFoundException() {
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> contractService.getContract(0L));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    assertThat(exception.getReason(), equalTo("Contract not found!"));
  }
}