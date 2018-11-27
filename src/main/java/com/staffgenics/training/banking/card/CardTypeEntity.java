package com.staffgenics.training.banking.card;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cardType")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class CardTypeEntity {

  @Id
  private String code;

  private String label;

  static CardTypeEntity createInstance(CardTypeDto cardTypeDto) {
    CardTypeEntity cardTypeEntity = new CardTypeEntity();
    cardTypeEntity.setCode(cardTypeDto.getCode());
    cardTypeEntity.setLabel(cardTypeDto.getLabel());
    return cardTypeEntity;
  }
}
