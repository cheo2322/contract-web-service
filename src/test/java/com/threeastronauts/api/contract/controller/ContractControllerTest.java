package com.threeastronauts.api.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Invoice.Status;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import com.threeastronauts.api.contract.service.ContractService;
import com.threeastronauts.api.contract.service.InvoiceService;
import org.junit.jupiter.api.BeforeAll;
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
  InvoiceService invoiceService;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractRepository contractRepository;

  @Autowired
  ObjectMapper objectMapper;

  private static Client client;
  private static Vendor vendor;
  private static ContractPostRequest contractPostRequest;

  private static Contract contract;
  private static InvoicePostRequest invoicePostRequest;

  @BeforeAll
  static void setUp() {
    client = ContractTestHelper.createClient();
    vendor = ContractTestHelper.createVendor();
    contractPostRequest = ContractTestHelper.createContractPostRequest();

    contract = ContractTestHelper.createContract();
    invoicePostRequest = ContractTestHelper.createInvoicePostRequest();
  }

  @Test
  void shouldReturn201Response_whenAClientCreatesANewContract() throws Exception {
    clientRepository.save(client);
    vendorRepository.save(vendor);

    mockMvc.perform(MockMvcRequestBuilders.post("/contract-api/contracts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(contractPostRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void shouldReturnAContract_whenIsCalledByAVendor() throws Exception {
    client.setId(2L);
    vendor.setId(2L);

    clientRepository.save(client);
    vendorRepository.save(vendor);

    contractService.setUpNewContract(contractPostRequest);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/contract-api/contracts")
            .param("vendorId", String.valueOf(2L))
            .param("contractId", String.valueOf(2L)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.terms").value("Terms."));
  }

  @Test
  void shouldReturn201Response_whenAVendorCreatesANewInvoice() throws Exception {
    contract.setId(7L);
    Client client1 = Client.builder().build();
    Vendor vendor1 = Vendor.builder().build();

    contract.setClient(client1);
    contract.setVendor(vendor1);

    clientRepository.save(client1);
    vendorRepository.save(vendor1);
    contractRepository.save(contract);

    mockMvc.perform(MockMvcRequestBuilders.post("/contract-api/invoices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoicePostRequest)))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  void shouldReturnAnInvoice_whenIsCalledByAClient() throws Exception {
    Client client1 = Client.builder().build();
    Vendor vendor1 = Vendor.builder().build();

    contract.setClient(client1);
    contract.setVendor(vendor1);

    clientRepository.save(client1);
    vendorRepository.save(vendor1);
    contractRepository.save(contract);

    invoiceService.createNewInvoice(invoicePostRequest);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/contract-api/invoices")
            .param("clientId", String.valueOf(1L))
            .param("invoiceId", String.valueOf(1L)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.timeInHours").value(10.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hourCost").value(4.9))
        .andExpect(MockMvcResultMatchers.jsonPath("$.otherMaterials").value("Materials"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.otherMaterialsCost").value(1.0))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Status.APPROVED.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0.0))
    ;
  }
}