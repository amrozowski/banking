package com.staffgenics.training.banking.card;

import com.staffgenics.training.banking.BankingProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceMockitoTest {

  @Mock
  private CardRepository cardRepository;

  @Mock
  private CardTypeRepository cardTypeRepository;

  @Mock
  private CardNumberService cardNumberService;

  private CardService cardService;

  private CardEntity cardEntity;

  private BankingProperties bankingProperties;

  private CardTypeEntity cardTypeEntity;


  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    bankingProperties = new BankingProperties();
    cardService = new CardService(cardRepository, cardNumberService, bankingProperties, cardTypeRepository);
    Mockito.when(cardRepository.save(Mockito.any(CardEntity.class))).thenAnswer((Answer<CardEntity>) invocation -> {
      cardEntity = invocation.getArgument(0);
      cardEntity.setId(1L);
      return cardEntity;
    });
    Mockito.when(cardRepository.findCardByAccountIdAndType(Mockito.any(Long.class), Mockito.any(String.class))).thenAnswer((Answer<Optional<List<CardEntity>>>) invocation -> {
      cardEntity = new CardEntity();
      cardEntity.setId(1L);
      List<CardEntity> cardList =  new ArrayList<>();
      return Optional.of(cardList);
    });
    Mockito.when(cardTypeRepository.findByCode(Mockito.any(String.class))).thenAnswer((Answer<Optional<CardTypeEntity>>) invocation -> {
      cardTypeEntity = new CardTypeEntity();
      cardTypeEntity.setCode(invocation.getArgument(0));
      return Optional.of(cardTypeEntity);
    });
  }

  @Test
  public void createCardSuccess(){
    //given
    CardDto cardDto = new CardDto();
    cardDto.setType("DEBI");
    cardDto.setAccountId(1L);
    Mockito.when(cardNumberService.generateUniqueNumber()).thenReturn("123456789");
    Mockito.when(cardNumberService.generateCvv()).thenReturn("999");

    //when
    Long id = cardService.createCard(cardDto);

    //then
    Assert.assertEquals("123456789", cardEntity.getCardNumber());
    Assert.assertEquals("999", cardEntity.getCvvCode());
    Assert.assertEquals(cardDto.getType(), cardEntity.getCardType().getCode());
    Assert.assertEquals(Long.valueOf(1L), id);
    Assert.assertEquals(cardDto.getAccountId(), cardEntity.getAccountId());
  }
}
