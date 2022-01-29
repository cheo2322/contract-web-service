package com.threeastronauts.api.contract.dto;

import com.threeastronauts.api.contract.model.Invoice.Status;
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

  private String description;
  private double timeInHours;
  private double hourCost;
  private String otherMaterials;
  private double otherMaterialsCost;
  private Status status;

  @NotNull
  private Double total;
}
