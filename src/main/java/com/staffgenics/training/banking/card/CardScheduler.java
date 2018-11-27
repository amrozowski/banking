package com.staffgenics.training.banking.card;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardScheduler {

  private final CardService cardService;

  @Autowired
  public CardScheduler(CardService cardService) {
    this.cardService = cardService;
  }


  @Scheduled(cron = "${banking.cronPeriodity}")
  void executeJob(){
    log.info("verifyAllCardsExpireTime");
    cardService.verifyAllCardsExpireTime();
  }
}
