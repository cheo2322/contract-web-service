package com.threeastronauts.api.contract.helper;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.domain.request.InvoicePostRequest;
import com.threeastronauts.api.contract.dto.ClientDto;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.dto.ContractInvoiceDto;
import com.threeastronauts.api.contract.dto.InvoiceDto;
import com.threeastronauts.api.contract.dto.VendorDto;
import com.threeastronauts.api.contract.model.Client;
import com.threeastronauts.api.contract.model.Contract;
import com.threeastronauts.api.contract.model.Vendor;
import java.util.List;

public class ContractTestHelper {

  public static Client createClient() {
    return Client.builder()
        .username("client")
        .contracts(List.of())
        .build();
  }

  public static Vendor createVendor() {
    return Vendor.builder()
        .username("vendor")
        .contracts(List.of())
        .build();
  }

  public static Contract createContract() {
    return Contract.builder()
        .approved(1)
        .terms("Terms.")
        .build();
  }

  public static ContractPostRequest createContractPostRequest() {
    return ContractPostRequest.builder()
        .client(ClientDto.builder()
            .username("client")
            .build())
        .vendor(VendorDto.builder()
            .username("vendor")
            .build())
        .contract(ContractDto.builder()
            .value(100.00)
            .terms("Terms.")
            .build())
        .build();
  }

  public static InvoicePostRequest createInvoicePostRequest() {
    return InvoicePostRequest.builder()
        .invoice(InvoiceDto.builder()
            .timeInHours(10)
            .hourCost(4.9)
            .otherMaterials("Materials")
            .otherMaterialsCost(1.0)
            .total(0.0)
            .build())
        .vendor(VendorDto.builder()
            .username("test-vendor")
            .build())
        .contract(ContractInvoiceDto.builder()
            .id(1L)
            .build())
        .build();
  }
}
