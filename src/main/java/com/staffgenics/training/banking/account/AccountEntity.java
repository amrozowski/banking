package com.staffgenics.training.banking.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long clientId;

  private String accountNumber;

  @Enumerated(EnumType.STRING)
  private Currency currency;

  private BigDecimal balance;

  static AccountEntity createInstance(AccountDto accountDto) {
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setClientId(accountDto.getClientId());
    accountEntity.setAccountNumber(accountDto.getAccountNumber());
    accountEntity.setCurrency(Currency.valueOf(accountDto.getCurrency()));
    accountEntity.setBalance(accountDto.getBalance());
    return accountEntity;
  }

  void update(AccountDto accountDto) {
    setClientId(accountDto.getClientId());
    setAccountNumber(accountDto.getAccountNumber());
    setCurrency(Currency.valueOf(accountDto.getCurrency()));
    setBalance(accountDto.getBalance());
  }

  void subtractBalance(BigDecimal amount){
    BigDecimal newBalance = balance.subtract(amount);
    setBalance(newBalance);
  }

  void addBalance(BigDecimal amount){
    BigDecimal newBalance = balance.add(amount);
    setBalance(newBalance);
  }
}
