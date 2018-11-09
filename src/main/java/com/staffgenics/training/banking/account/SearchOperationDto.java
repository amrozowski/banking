package com.staffgenics.training.banking.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Getter
public class SearchOperationDto {
  private BigDecimal amountFrom;
  private BigDecimal amountTo;
  private Date dateFrom;
  private Date dateTo;
  private String operationType;
}
