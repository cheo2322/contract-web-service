package com.threeastronauts.api.contract.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.threeastronauts.api.contract.domain.request.InvoicePatchRequest;
import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.ContractInvoiceDto;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.exception.ContractValueExceedException;
import com.threeastronauts.api.contract.exception.ResourceNotFoundException;
import com.threeastronauts.api.contract.helper.ContractTestHelper;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Invoice;
import com.threeastronauts.api.contract.model.Invoice.Status;
import com.threeastronauts.api.contract.model.Vendor;
import com.threeastronauts.api.contract.repository.ClientRepository;
import com.threeastronauts.api.contract.repository.ContractRepository;
import com.threeastronauts.api.contract.repository.InvoiceRepository;
import com.threeastronauts.api.contract.repository.VendorRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

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

  private Client client;
  private Vendor vendor;
  private Contract contract;
  private InvoicePostRequest invoicePostRequest;

  @BeforeEach
  void setUp() {
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

    invoicePostRequest.setContract(ContractInvoiceDto.builder().id(contract.getId()).build());

    clientRepository.save(client);
    vendorRepository.save(vendor);
    contractRepository.save(contract);

    invoiceService.createNewInvoice(invoicePostRequest);

    InvoiceDto invoice = invoiceService.getInvoice(1L);

    assertThat(invoice.getTimeInHours(), equalTo(10.0));
  }

  @Test
  void shouldUpdateAnInvoiceStatusToVoid() {
    Invoice invoice = Invoice.builder()
        .id(1L)
        .status(Status.APPROVED)
        .build();

    InvoicePatchRequest request = InvoicePatchRequest.builder()
        .id(invoice.getId())
        .status(Status.VOID)
        .build();

    invoiceRepository.save(invoice);
    invoiceService.updateInvoiceStatus(request);
    InvoiceDto invoiceUpdated = invoiceService.getInvoice(invoice.getId());

    assertThat(invoiceUpdated, notNullValue());
    assertThat(invoiceUpdated.getStatus(), equalTo(Status.VOID));
  }

  @Test
  void shouldThrowsVendorResourceNotFoundException() {
    invoicePostRequest.getContract().setId(-1L);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> invoiceService.createNewInvoice(invoicePostRequest));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
  }

  @Test
  void shouldThrowsContractResourceNotFoundException() {
    Vendor vendor = Vendor.builder()
        .id(10L)
        .username("client0")
        .build();

    invoicePostRequest.getContract().setId(-1L);

    vendorRepository.save(vendor);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> invoiceService.createNewInvoice(invoicePostRequest));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    assertThat(exception.getReason(), equalTo("Contract not found!"));
  }

  @Test
  void shouldReturnContractResourceNotFoundException() {
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
        () -> invoiceService.getInvoice(0L));

    assertThat(exception.getStatus(), equalTo(HttpStatus.NOT_FOUND));
    assertThat(exception.getReason(), equalTo("Invoice not found!"));
  }

  @Test
  void shouldThrowsAContractValueExceedException() {
    vendor.setUsername("error");
    contract.setClient(client);
    contract.setVendor(vendor);

    invoicePostRequest.getInvoice().setTotal(200.0);

    clientRepository.save(client);
    vendorRepository.save(vendor);
    contractRepository.save(contract);

    ContractValueExceedException exception = assertThrows(ContractValueExceedException.class,
        () -> invoiceService.createNewInvoice(invoicePostRequest));

    assertThat(exception.getExceedValue(), equalTo(100.0));
  }
}