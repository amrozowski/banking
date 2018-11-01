package com.staffgenics.training.banking.account;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.staffgenics.training.banking.account.operation.OperationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
  
  private final AccountRepository accountRepository;
  
  @Autowired
  AccountService(AccountRepository accountRepository){
    this.accountRepository = accountRepository;
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

  private AccountEntity findAccountByNumber(OperationDto operationDto){
    AccountEntity destinationAccount = accountRepository.findBy
    return new AccountEntity();
  }

  Long addOperation(OperationDto operationDto, Long id){
    AccountEntity sourceAccount = findAccount(id);
    if(sourceAccount.getBalance().compareTo(operationDto.getAmount()) < 0){
      throw new IllegalArgumentException("Brak środków na koncie");
    }
    AccountEntity destiantionAccount = (operationDto.getId());


    return new Long(0);
  }
}
