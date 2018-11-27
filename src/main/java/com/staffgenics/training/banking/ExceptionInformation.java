package com.staffgenics.training.banking;

import lombok.Getter;

import java.util.Date;

@Getter
class ExceptionInformation {

  private String message;
  private Date exceptionDate;

  ExceptionInformation(String message) {
    this.exceptionDate = new Date();
    this.message = message;
  }
}
