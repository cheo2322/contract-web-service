package com.threeastronauts.api.contract.domain.error;

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
public class ErrorResponse {

  private String code;
  private String developerMessage;
  private String userMessage;
  private String moreInfo;
}
