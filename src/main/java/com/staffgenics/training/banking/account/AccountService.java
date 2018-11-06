package com.staffgenics.training.banking.account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountService {
  
  private final AccountRepository accountRepository;
  private final OperationRepository operationRepository;
  private final CurrencyRepository currencyRepository;
  private static final int MAX_GENERATION_TRIES = 1000;
  
  @Autowired
  AccountService(AccountRepository accountRepository, OperationRepository operationRepository, CurrencyRepository currencyRepository){
    this.accountRepository = accountRepository;
    this.operationRepository = operationRepository;
    this.currencyRepository = currencyRepository;
  }

  List<AccountDto> getAccounts(){
    log.info("Pobieramy wszystkie konta");
    return accountRepository.findAll().stream()
        .map(AccountDto::createInstance)
        .collect(Collectors.toList());
  }
  private String generateUniqueNrb() {
    String accountNumber;
    int generationTry = 0;
    do {
      if (MAX_GENERATION_TRIES < generationTry) {
        throw new IllegalStateException("Błąd generacji konta, przekroczono maksymalną liczbę prób");
      }
      Iban iban = new Iban.Builder()
          .countryCode(CountryCode.PL)
          .bankCode("19043")
          .buildRandom();
      accountNumber = iban.getAccountNumber();
      generationTry++;
    } while (accountRepository.findByNRB(accountNumber).get() != null);
    return accountNumber;
  }

  Long createAccount(AccountDto accountDto) {
    log.info("Dodajemy nowe konto");
    String accountNumber = generateUniqueNrb();
    accountDto.setAccountNumber(accountNumber);
    CurrencyEntity currencyEntity = currencyRepository.getOne(accountDto.getCurrency());
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto, currencyEntity);
    accountRepository.save(accountEntity);
    return accountEntity.getId();
  }

  AccountDto getAccount(Long id) {
    log.info("Pobieramy konto o id: {}", id);
    AccountEntity accountEntity = findAccount(id);
    return AccountDto.createInstance(accountEntity);
  }

  private AccountEntity findAccount(Long id) {
    Optional<AccountEntity> accountEntityOptional = accountRepository.findById(id);
    if (!accountEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Brak konta w bazie danych");
    }
    return accountEntityOptional.get();
  }

  private Optional<AccountEntity> findAccountByNRB(String accountNumber){
    Optional<AccountEntity> destinationAccount = accountRepository.findByNRB(accountNumber);
    return destinationAccount;
  }

  @Transactional
  Long addOperation(OperationDto operationDto, Long id){
    AccountEntity sourceAccount = findAccount(id);
    BigDecimal sourceAccountBalance = sourceAccount.getBalance();
    if(sourceAccountBalance.compareTo(operationDto.getAmount()) < 0){
      throw new IllegalArgumentException("Brak środków na koncie");
    }
    Optional<AccountEntity> destiantionAccount = findAccountByNRB(operationDto.getDestinationAccountNumber());
    if(!destiantionAccount.isPresent()){
      throw new IllegalArgumentException("Brak konta w bazie danych");
    }
    sourceAccount.subtractBalance(operationDto.getAmount());
    destiantionAccount.get().addBalance(operationDto.getAmount());
    OperationEntity operationEntity = OperationEntity.createInstance(operationDto);
    accountRepository.save(sourceAccount);
    accountRepository.save(destiantionAccount.get());
    operationRepository.save(operationEntity);
    return operationEntity.getId();
  }

  List<OperationDto> findOperationsOut(SearchOperationDto searchOperationDto, Long id){
    return operationRepository.findOutByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), id).stream()
        .map(OperationDto::createInstance)
        .collect(Collectors.toList());
  }

  List<OperationDto> findOperationsIncome(SearchOperationDto searchOperationDto, Long id){
    Optional<AccountEntity> accountEntityOptional = accountRepository.findById(id);
    String nrb = accountEntityOptional.get().getAccountNumber();
    return operationRepository.findIncomeByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), nrb).stream()
        .map(OperationDto::createInstance)
        .collect(Collectors.toList());
  }
}
