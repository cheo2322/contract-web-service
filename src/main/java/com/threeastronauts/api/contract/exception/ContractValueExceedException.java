package com.threeastronauts.api.contract.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ContractValueExceedException extends ResponseStatusException {

  private final Double exceedValue;

  public ContractValueExceedException(HttpStatus status, Double exceedValue) {
    super(status);
    this.exceedValue = exceedValue;
  }
}
