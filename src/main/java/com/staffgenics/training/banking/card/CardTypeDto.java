package com.staffgenics.training.banking.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CardTypeDto {

  private String code;

  private String label;

  static CardTypeDto createInstance(CardTypeEntity cardTypeEntity){
    CardTypeDto cardTypeDto = new CardTypeDto();
    cardTypeDto.setCode(cardTypeEntity.getCode());
    cardTypeDto.setLabel(cardTypeEntity.getLabel());
    return cardTypeDto;
  }
}
