package com.staffgenics.training.banking.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService){
    this.cardService = cardService;
  }

  @RequestMapping(value = "/card", method = RequestMethod.POST)
  public Long createCard (@RequestBody CardDto cardDto){
    return cardService.createCard(cardDto);
  }
}
