package com.threeastronauts.api.contract.domain.request;

import com.threeastronauts.api.contract.dto.ClientDto;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.dto.VendorDto;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractPostRequest {

  @NotEmpty
  private ClientDto client;

  @NotEmpty
  private ContractDto contract;

  @NotEmpty
  private VendorDto vendor;
}
