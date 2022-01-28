package com.threeastronauts.api.contract.helper;

import com.threeastronauts.api.contract.domain.request.ContractPostRequest;
import com.threeastronauts.api.contract.dto.ClientDto;
import com.threeastronauts.api.contract.dto.ContractDto;
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
        .terms("Terms")
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
            .terms("Terms.")
            .build())
        .build();
  }
}
