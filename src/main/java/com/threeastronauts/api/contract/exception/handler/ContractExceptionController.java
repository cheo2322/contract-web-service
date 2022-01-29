package com.threeastronauts.api.contract.exception.handler;

import com.threeastronauts.api.contract.domain.error.ErrorResponse;
import com.threeastronauts.api.contract.exception.ResourceNotFoundException;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ContractExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex) {

        log.error(ex.getAllErrors().toString());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("ERR-VAL-001")
            .developerMessage(
                "Some errors have been occurred with data. Please refer moreInfo section.")
            .userMessage("Sorry! An internal error has been occurred!")
            .moreInfo("moreInfo/ERR-VAL-001/solution")
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(
        ResourceNotFoundException ex) {

        log.error(ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
            .code("ERR-DB-004")
            .developerMessage(
                Objects.requireNonNull(ex.getReason()).concat(" Please refer moreInfo section."))
            .userMessage(ex.getReason())
            .moreInfo("moreInfo/ERR-DB-004/solution")
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
