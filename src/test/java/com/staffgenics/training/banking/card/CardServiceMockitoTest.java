package com.staffgenics.training.banking.card;

import com.staffgenics.training.banking.account.AccountEntity;
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

@RunWith(MockitoJUnitRunner.class)
public class CardServiceMockitoTest {

  @Mock
  private CardRepository cardRepository;

  @Mock
  private CardNumberService cardNumberService;

  private CardService cardService;

  private CardEntity cardEntity;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    cardService = new CardService(cardRepository, cardNumberService);
    Mockito.when(cardRepository.save(Mockito.any(CardEntity.class))).thenAnswer((Answer<CardEntity>) invocation -> {
      cardEntity = invocation.getArgument(0);
      cardEntity.setId(1L);
      return cardEntity;
    });
  }

  @Test
  public void createCardSuccess(){
    //given
    CardDto cardDto = new CardDto();
    cardDto.setType("TYPE");
    cardDto.setAccountId(1L);
    Mockito.when(cardNumberService.generateUniqueNumber()).thenReturn("123456789");
    Mockito.when(cardNumberService.generateCvv()).thenReturn("999");

    //when
    Long id = cardService.createCard(cardDto);

    //then
    Assert.assertEquals("123456789", cardEntity.getCardNumber());
    Assert.assertEquals("999", cardEntity.getCvvCode());
    Assert.assertEquals(cardDto.getType(), cardEntity.getType());
    Assert.assertEquals(Long.valueOf(1L), id);
    Assert.assertEquals(cardDto.getAccountId(), cardEntity.getAccountId());
  }
}
