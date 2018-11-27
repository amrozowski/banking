package com.staffgenics.training.banking.card;

import com.staffgenics.training.banking.BankingProperties;
import com.staffgenics.training.banking.account.AccountDto;
import com.staffgenics.training.banking.exception.ClientBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

  private final CardRepository cardRepository;

  private CardNumberService cardNumberService;

  private BankingProperties bankingProperties;

  private CardTypeRepository cardTypeRepository;

  @Autowired
  CardService(CardRepository cardRepository, CardNumberService cardNumberService, BankingProperties bankingProperties, CardTypeRepository cardTypeRepository){
    this.cardRepository = cardRepository;
    this.cardNumberService = cardNumberService;
    this.bankingProperties = bankingProperties;
    this.cardTypeRepository = cardTypeRepository;
  }
  Long createCard(CardDto cardDto){
    verifyCard(cardDto);
    int cardValidMonths = bankingProperties.getCardValidMonths();
    LocalDate validThruDate = LocalDate.now().plusMonths(cardValidMonths);
    CardTypeEntity type = cardTypeRepository.findByCode(cardDto.getType()).get();
    CardEntity cardEntity = CardEntity.createInstance(cardDto, validThruDate, cardNumberService.generateUniqueNumber(), cardNumberService.generateCvv(), type);
    cardRepository.save(cardEntity);
    return cardEntity.getId();
  }

  private void verifyCard(CardDto cardDto){
    Optional<List<CardEntity>> cardsEntityOptional = cardRepository.findCardByAccountIdAndType(cardDto.getAccountId(), cardDto.getType());
    if(cardsEntityOptional.isPresent()) {
      int count = cardsEntityOptional.get().size();
      if (count >= bankingProperties.getMaxCardAmountOfSameType()) {
        throw new ClientBadRequestException("Dla podanego konta istnieje już karta tego typu - ClientBadRequestException");
      }
    }
  }

  void verifyAllCardsExpireTime(){
    Optional<List<CardEntity>> optionalExpiredCards = cardRepository.findExpiredCards(LocalDate.now());
    if(optionalExpiredCards.isPresent()){
      for (CardEntity card : optionalExpiredCards.get()) {
        card.setActive(false);
      }
      cardRepository.saveAll(optionalExpiredCards.get());
    }
  }

  List<CardTypeDto> showCardTypes(){
    return cardTypeRepository.findAll().stream()
        .map(CardTypeDto::createInstance)
        .collect(Collectors.toList());
  }

  String createCardType(CardTypeDto cardTypeDto){
    CardTypeEntity cardTypeEntity = CardTypeEntity.createInstance(cardTypeDto);
    cardTypeRepository.save(cardTypeEntity);
    return cardTypeEntity.getCode();
  }
}
