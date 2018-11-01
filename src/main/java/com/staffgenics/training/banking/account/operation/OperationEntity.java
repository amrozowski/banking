package com.staffgenics.training.banking.account.operation;

import javax.persistence.*;

import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "operation")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class OperationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long sourceAccountNumber;
  private String destinationAccountNumber;
  private BigDecimal amount;
  private String currency;
  private Date transactionDate;

  static OperationEntity createInstance(OperationDto operationDto) {
    OperationEntity operationEntity = new OperationEntity();
    operationEntity.setSourceAccountNumber(operationDto.getSourceAccountNumber());
    operationEntity.setDestinationAccountNumber(operationDto.getDestinationAccountNumber());
    operationEntity.setAmount(operationDto.getAmount());
    operationEntity.setCurrency(operationDto.getCurrency());
    operationEntity.setTransactionDate(operationDto.getTransactionDate());
    return operationEntity;
  }
}
