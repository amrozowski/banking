package com.staffgenics.training.banking.card;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class CardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private CardTypeEntity cardType;

  private String cardNumber;

  private String cvvCode;

  private Long accountId;

  private Date createDate;

  private LocalDate validThruDate;

  private boolean active;

  static CardEntity createInstance(CardDto cardDto, LocalDate validThruDate, String cardNumber, String cvv, CardTypeEntity type){
    CardEntity cardEntity = new CardEntity();
    cardEntity.setCardType(type);
    cardEntity.setCardNumber(cardNumber);
    cardEntity.setCvvCode(cvv);
    cardEntity.setAccountId(cardDto.getAccountId());
    cardEntity.setCreateDate(new Date());
    cardEntity.setValidThruDate(validThruDate);
    cardEntity.setActive(true);
    return cardEntity;
  }
}
