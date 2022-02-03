package com.threeastronauts.api.contract.domain.request;

import static com.threeastronauts.api.contract.model.Invoice.Status;

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
public class InvoicePatchRequest {

  @NotNull
  private Long id;

  @NotNull
  private Status status;
}
