package com.staffgenics.training.banking.account;

import com.staffgenics.training.banking.client.ClientEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private ClientEntity client;

  private String accountNumber;

  @Enumerated(EnumType.STRING)
  private Currency currency;

  private BigDecimal balance;
}
