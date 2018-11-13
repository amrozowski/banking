package com.staffgenics.training.banking.account;

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

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Testy jednostkowe dla klasy {@link AccountService}.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceMockitoTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private OperationRepository operationRepository;

  @Mock
  private CurrencyRepository currencyRepository;

  @Mock
  private NrbService nrbService;

  @Mock
  private OperationService operationService;

  private AccountService accountService;

  private AccountEntity sourceAccountEntity = new AccountEntity();
  private AccountEntity destiantionAccountEntity = new AccountEntity();

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() {
    accountService = new AccountService(accountRepository, operationRepository, currencyRepository, nrbService, operationService);
  }

  @Test
  public void referOperationSuccess() {
    //given
    OperationDto operationDto = new OperationDto();
    operationDto.setDestinationAccountNumber("70 1140 2004 0000 3105");
    operationDto.setCurrency("PLN");
    BigDecimal sourceAccountBalance = new BigDecimal(1000);
    BigDecimal destinationAccountBalance = new BigDecimal(0);
    BigDecimal amount = new BigDecimal(100);
    operationDto.setAmount(amount);
    Long sourceAccountId = 1L;
    Long destinationAccountId = 2L;
    Mockito.when(accountRepository.findById(sourceAccountId)).thenAnswer((Answer<Optional<AccountEntity>>) invocation -> {
      sourceAccountEntity.setId(invocation.getArgument(0));
      if(sourceAccountEntity.getBalance() == null){
        sourceAccountEntity.setBalance(sourceAccountBalance);
      }
      sourceAccountEntity.setVersion(0L);
      Optional<AccountEntity> accountEnitityOptional;
      accountEnitityOptional = Optional.of(sourceAccountEntity);
      return accountEnitityOptional;
    });
    Mockito.when(accountRepository.findById(destinationAccountId)).thenAnswer((Answer<Optional<AccountEntity>>) invocation -> {
      destiantionAccountEntity.setId(invocation.getArgument(0));
      destiantionAccountEntity.setVersion(0L);
      Optional<AccountEntity> accountEnitityOptional;
      accountEnitityOptional = Optional.of(destiantionAccountEntity);
      return accountEnitityOptional;
    });
    Mockito.when(accountRepository.findByNRB(operationDto.getDestinationAccountNumber())).thenAnswer((Answer<Optional<AccountEntity>>) invocation -> {
      destiantionAccountEntity = new AccountEntity();
      destiantionAccountEntity.setAccountNumber(invocation.getArgument(0));
      destiantionAccountEntity.setBalance(destinationAccountBalance);
      destiantionAccountEntity.setId(2L);
      destiantionAccountEntity.setVersion(0L);
      Optional<AccountEntity> accountEnitityOptional;
      accountEnitityOptional = Optional.of(destiantionAccountEntity);
      return accountEnitityOptional;
    });

    //when
    accountService.referOperation(operationDto, sourceAccountId);

    //then
    Assert.assertEquals(sourceAccountBalance.subtract(amount), sourceAccountEntity.getBalance());
    Assert.assertEquals(destinationAccountBalance.add(amount), destiantionAccountEntity.getBalance());
  }
}
