package com.threeastronauts.api.contract.domain.request;

import com.threeastronauts.api.contract.dto.ClientDto;
import com.threeastronauts.api.contract.dto.ContractDto;
import com.threeastronauts.api.contract.dto.VendorDto;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

  @Valid
  @NotNull
  private ClientDto client;

  @Valid
  @NotNull
  private ContractDto contract;

  @Valid
  @NotNull
  private VendorDto vendor;
}
