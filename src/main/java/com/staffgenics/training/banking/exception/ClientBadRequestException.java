package com.staffgenics.training.banking.exception;

public class ClientBadRequestException extends RuntimeException {

  public ClientBadRequestException(String message) {
    super(message);
  }
}
