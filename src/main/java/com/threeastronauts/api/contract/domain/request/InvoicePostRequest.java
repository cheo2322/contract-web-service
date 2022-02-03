package com.threeastronauts.api.contract.domain.request;

import com.threeastronauts.api.contract.dto.ContractInvoiceDto;
import com.threeastronauts.api.contract.dto.InvoiceDto;
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
public class InvoicePostRequest {
  
  @NotNull
  private ContractInvoiceDto contract;

  @NotNull
  private InvoiceDto invoice;
}
