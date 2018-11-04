package com.staffgenics.training.banking.account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.staffgenics.training.banking.account.operation.OperationDto;
import com.staffgenics.training.banking.account.operation.OperationEntity;
import com.staffgenics.training.banking.account.operation.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountService {
  
  private final AccountRepository accountRepository;
  private final OperationRepository operationRepository;
  
  @Autowired
  AccountService(AccountRepository accountRepository, OperationRepository operationRepository){
    this.accountRepository = accountRepository;
    this.operationRepository = operationRepository;
  }

  List<AccountDto> getAccounts(){
    log.info("Pobieramy wszystkie konta");
    return accountRepository.findAll().stream()
        .map(AccountDto::createInstance)
        .collect(Collectors.toList());
  }

  Long createAccount(AccountDto accountDto) {
    log.info("Dodajemy nowe konto");
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto);
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

  void editAccount(AccountDto accountDto, Long id) {
    log.info("Edytujemy konto o id: {}");
    AccountEntity accountEntity = findAccount(id);
    accountEntity.update(accountDto);
    accountRepository.save(accountEntity);
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

  List<OperationDto> findOperations(SearchOperationDto searchOperationDto, Long id){
    return operationRepository.fin
  }
}
