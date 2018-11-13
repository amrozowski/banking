package com.staffgenics.training.banking.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardRepository cardRepository;

  private CardNumberService cardNumberService;

  @Autowired
  CardService(CardRepository cardRepository, CardNumberService cardNumberService){
    this.cardRepository = cardRepository;
    this.cardNumberService = cardNumberService;
  }
  Long createCard(CardDto cardDto){
    CardEntity cardEntity = CardEntity.createInstance(cardDto);
    cardEntity.setCardNumber(cardNumberService.generateUniqueNumber());
    cardEntity.setCvvCode(cardNumberService.generateCvv());
    cardRepository.save(cardEntity);
    return cardEntity.getId();
  }
}
