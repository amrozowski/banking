package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<OperationEntity, Long> {

  @Query("SELECT operation FROM OperationEntity operation " +
      "WHERE operation.sourceAccountId = :id " +
      "WHERE operation.amount >= :amountFrom " +
      "AND operation.amount <= :amountTo " +
      "AND operation.transactionDate >= :dateFrom " +
      "AND operation.transactionDate <= :dateTo")
  List<OperationEntity> findOutByAmountAndDate(BigDecimal amountFrom, BigDecimal amountTo, Date dateFrom, Date dateTo, Long id);

  @Query("SELECT operation FROM OperationEntity operation " +
      "WHERE operation.destinationAccountNumber = :accountNumber " +
      "AND operation.amount >= :amountFrom " +
      "AND operation.amount <= :amountTo " +
      "AND operation.transactionDate >= :dateFrom " +
      "AND operation.transactionDate <= :dateTo")
  List<OperationEntity> findIncomeByAmountAndDate(BigDecimal amountFrom, BigDecimal amountTo, Date dateFrom, Date dateTo, String accountNumber);
}
