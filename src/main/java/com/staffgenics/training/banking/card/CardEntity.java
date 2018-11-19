package com.staffgenics.training.banking.card;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

  private String type;

  private String cardNumber;

  private String cvvCode;

  private Long accountId;

  private Date createDate;

  private int validThru;

  static CardEntity createInstance(CardDto cardDto){
    CardEntity cardEntity = new CardEntity();
    cardEntity.setType(cardDto.getType());
    cardEntity.setAccountId(cardDto.getAccountId());
    cardEntity.setCreateDate(new Date());
    cardEntity.setValidThru(cardDto.getValidThru());
    return cardEntity;
  }
}
