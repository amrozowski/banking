package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.exception.ClientBadRequestException;
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
@Setter(AccessLevel.PACKAGE)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long clientId;

  private String accountNumber;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private CurrencyEntity currency;

  private BigDecimal balance;

  @Version
  private Long version;

  private boolean deleted;

  static AccountEntity createInstance(AccountDto accountDto, CurrencyEntity currencyEntity) {
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setClientId(accountDto.getClientId());
    accountEntity.setAccountNumber(accountDto.getAccountNumber());
    accountEntity.setCurrency(currencyEntity);
    accountEntity.setBalance(BigDecimal.ZERO);
    accountEntity.setVersion(accountDto.getVersion());
    accountEntity.setDeleted(accountDto.isDeleted());
    return accountEntity;
  }

  void subtractBalance(BigDecimal amount){
    if(balance.compareTo(amount) < 0){
      throw new ClientBadRequestException("Brak środków na koncie");
    }
    BigDecimal newBalance = balance.subtract(amount);
    setBalance(newBalance);
  }

  void addBalance(BigDecimal amount){
    BigDecimal newBalance = balance.add(amount);
    setBalance(newBalance);
  }

  BigDecimal getBalanceInPLN() {
    return balance.multiply(currency.getExchangeRate());
  }
}
