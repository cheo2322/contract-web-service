package com.threeastronauts.api.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import com.threeastronauts.api.contract.service.ContractService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ContractControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ContractService contractService;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void shouldReturn201Response_whenAClientCreateANewContract() throws Exception {
    ContractPostRequest contractPostRequest = ContractTestHelper.createContractPostRequest();

    Client client = ContractTestHelper.createClient();
    Vendor vendor = ContractTestHelper.createVendor();

    clientRepository.save(client);
    vendorRepository.save(vendor);

    mockMvc.perform(MockMvcRequestBuilders.post("/contract-api/contracts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(contractPostRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void shouldReturnAContract_whenIsCalledByAVendor() throws Exception {
    Client client = ContractTestHelper.createClient();
    Vendor vendor = ContractTestHelper.createVendor();
    ContractPostRequest contractPostRequest = ContractTestHelper.createContractPostRequest();

    clientRepository.save(client);
    vendorRepository.save(vendor);

    contractService.setUpNewContract(contractPostRequest);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/contract-api/{vendorId}/contracts/{contractId}", 1L, 1L))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.terms").value("Terms."));
  }
}