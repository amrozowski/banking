package com.staffgenics.training.banking.account.operation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class OperationDto {

  private Long id;
  private Long sourceAccountNumber;
  private String destinationAccountNumber;
  private BigDecimal amount;
  private String currency;
  private Date transactionDate;

  static OperationDto createInstance(OperationEntity operationEntity) {
    OperationDto operationDto = new OperationDto();
    operationDto.setId(operationEntity.getId());
    operationDto.setSourceAccountNumber(operationEntity.getSourceAccountNumber());
    operationDto.setDestinationAccountNumber(operationEntity.getDestinationAccountNumber());
    operationDto.setAmount(operationEntity.getAmount());
    operationDto.setCurrency(operationEntity.getCurrency());
    operationDto.setTransactionDate(operationEntity.getTransactionDate());
    return operationDto;
  }
}
