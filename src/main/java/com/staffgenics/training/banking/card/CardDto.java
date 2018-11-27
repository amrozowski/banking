package com.staffgenics.training.banking.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CardDto {

  private Long id;

  private String type;

  private String cardNumber;

  private String cvvCode;

  private Long accountId;

  private Date createDate;

  private LocalDate validThruDate;

  private boolean active;

  static CardDto createInstance(CardEntity cardEntity){
    CardDto cardDto = new CardDto();
    cardDto.setId(cardEntity.getId());
    cardDto.setType(cardEntity.getCardType().toString());
    cardDto.setCardNumber(cardEntity.getCardNumber());
    cardDto.setCvvCode(cardEntity.getCvvCode());
    cardDto.setAccountId(cardEntity.getAccountId());
    cardDto.setCreateDate(cardEntity.getCreateDate());
    cardDto.setValidThruDate(cardEntity.getValidThruDate());
    cardDto.setActive(cardEntity.isActive());
    return cardDto;
  }
}
