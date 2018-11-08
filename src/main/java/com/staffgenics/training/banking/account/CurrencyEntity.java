package com.staffgenics.training.banking.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Encja słownika walut.
 */
@Entity
@Table(name = "currency")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
class CurrencyEntity {

  @Id
  private String code;

  private String label;

  private BigDecimal exchangeRate;
}
