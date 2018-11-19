package com.staffgenics.training.banking.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CardNumberService {
  private final CardRepository cardRepository;
  private static final int MAX_GENERATION_TRIES = 1000;

  @Autowired
  CardNumberService(CardRepository cardRepository){
    this.cardRepository = cardRepository;
  }

  String generateUniqueNumber() {
    String cardNumber;
    int generationTry = 0;
    do {
      if (MAX_GENERATION_TRIES < generationTry) {
        throw new IllegalStateException("Błąd generacji numeru karty, przekroczono maksymalną liczbę prób");
      }
      Random rand = new Random();
      cardNumber = String.format ("%08d", rand.nextInt(100000000));
      cardNumber += String.format ("%08d", rand.nextInt(100000000));
      generationTry++;
    } while (checkCardNumberInRepository(cardNumber));
    return cardNumber;
  }
  private boolean checkCardNumberInRepository(String cardNumber){
    return cardRepository.findCardByCardNumber(cardNumber).isPresent();
  }

  String generateCvv(){
    return String.format ("%03d", new Random().nextInt(1000));
  }

}
