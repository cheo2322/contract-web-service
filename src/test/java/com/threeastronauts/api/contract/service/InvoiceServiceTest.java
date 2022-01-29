package com.threeastronauts.api.contract.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.ContractInvoiceDto;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.dto.VendorDto;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.InvoiceRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvoiceServiceTest {

  @Autowired
  InvoiceService invoiceService;

  @Autowired
  VendorRepository vendorRepository;

  @Autowired
  ContractRepository contractRepository;

  @Autowired
  ClientRepository clientRepository;

  @Autowired
  InvoiceRepository invoiceRepository;

  private static Client client;
  private static Vendor vendor;
  private static Contract contract;
  private static InvoicePostRequest invoicePostRequest;

  @BeforeAll
  static void setUp() {
    client = ContractTestHelper.createClient();
    vendor = ContractTestHelper.createVendor();
    contract = ContractTestHelper.createContract();
    invoicePostRequest = ContractTestHelper.createInvoicePostRequest();
  }

  @Test
  void shouldResponseCreated() {
    vendor.setUsername("invoice-service-vendor");
    contract.setClient(client);
    contract.setVendor(vendor);

    invoicePostRequest.getVendor().setUsername(vendor.getUsername());

    clientRepository.save(client);
    vendorRepository.save(vendor);
    contractRepository.save(contract);

    invoiceService.createNewInvoice(invoicePostRequest);

    assertThat(invoiceRepository.findById(1L), Matchers.anything());
  }

  @Test
  void shouldReturnAnInvoice() {
    vendor.setUsername("get-invoice-service");
    contract.setId(1L);
    contract.setClient(client);
    contract.setVendor(vendor);

    invoicePostRequest.setVendor(VendorDto.builder().username(vendor.getUsername()).build());
    invoicePostRequest.setContract(ContractInvoiceDto.builder().id(contract.getId()).build());

    clientRepository.save(client);
    vendorRepository.save(vendor);
    contractRepository.save(contract);

    invoiceService.createNewInvoice(invoicePostRequest);

    InvoiceDto invoice = invoiceService.getInvoice(1L);

    assertThat(invoice.getTimeInHours(), equalTo(10.0));
  }
}