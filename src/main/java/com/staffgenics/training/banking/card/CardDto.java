package com.staffgenics.training.banking.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  private int validThru;

  static CardDto createInstance(CardEntity cardEntity){
    CardDto cardDto = new CardDto();
    cardDto.setId(cardEntity.getId());
    cardDto.setType(cardEntity.getType());
    cardDto.setCardNumber(cardEntity.getCardNumber());
    cardDto.setCvvCode(cardEntity.getCvvCode());
    cardDto.setAccountId(cardEntity.getAccountId());
    cardDto.setCreateDate(cardEntity.getCreateDate());
    cardDto.setValidThru(cardEntity.getValidThru());
    return cardDto;
  }
}
