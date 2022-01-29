package com.threeastronauts.api.contract.dto;

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
public class InvoiceDto {

  private double timeInHours;
  private double hourCost;
  private String otherMaterials;
  private double otherMaterialsCost;

  @NotNull
  private double total;
}
