package com.staffgenics.training.banking.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    verifyCard(cardDto);
    CardEntity cardEntity = CardEntity.createInstance(cardDto);
    cardEntity.setCardNumber(cardNumberService.generateUniqueNumber());
    cardEntity.setCvvCode(cardNumberService.generateCvv());
    cardRepository.save(cardEntity);
    return cardEntity.getId();
  }

  private void verifyCard(CardDto cardDto){
    Optional<CardEntity> cardEntityOptional = cardRepository.findCardByAccountId(cardDto.getAccountId());
    if(cardEntityOptional.isPresent()) {
      if (cardDto.getAccountId().equals(cardEntityOptional.get().getAccountId())) {
        throw new IllegalArgumentException("Dla podanego konta istnieje już karta tego typu");
      }
    }
  }
}
