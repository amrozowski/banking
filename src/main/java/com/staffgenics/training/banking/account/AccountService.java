package com.staffgenics.training.banking.account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.OptimisticLockException;

@Service
@Slf4j
public class AccountService {
  
  private final AccountRepository accountRepository;
  private final OperationRepository operationRepository;
  private final CurrencyRepository currencyRepository;
  private NrbService nrbService;
  private OperationService operationService;
  
  @Autowired
  AccountService(AccountRepository accountRepository, OperationRepository operationRepository, CurrencyRepository currencyRepository, NrbService nrbService, OperationService operationService){
    this.accountRepository = accountRepository;
    this.operationRepository = operationRepository;
    this.currencyRepository = currencyRepository;
    this.nrbService = nrbService;
    this.operationService = operationService;
  }

  List<AccountDto> getAccounts(){
    log.info("Pobieramy wszystkie konta");
    return accountRepository.findAll().stream()
        .map(AccountDto::createInstance)
        .collect(Collectors.toList());
  }

  Long createAccount(AccountDto accountDto) {
    log.info("Dodajemy nowe konto");
    String accountNumber = nrbService.generateUniqueNrb();
    accountDto.setAccountNumber(accountNumber);
    CurrencyEntity currencyEntity = currencyRepository.getOne(accountDto.getCurrency());
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto, currencyEntity);
    accountRepository.save(accountEntity);
    return accountEntity.getId();
  }

  AccountDto getAccount(Long id) {
    log.info("Pobieramy konto o id: {}", id);
    AccountEntity accountEntity = accountRepository.findById(id).get();
    return AccountDto.createInstance(accountEntity);
  }

  @Transactional
  Long referOperation(OperationDto operationDto, Long id){
    OperationEntity operationEntity = OperationEntity.createInstance(operationDto);
    AccountEntity sourceAccount = verifySourceAccountBalance(id, operationEntity);
    AccountEntity destinationAccount = verifyDestinationAccountExist(operationEntity);
    updateAccounts(sourceAccount, destinationAccount, operationEntity.getAmount());
    operationService.addOperation(operationEntity);
    return operationEntity.getId();
  }

  private AccountEntity verifySourceAccountBalance(Long id, OperationEntity operationEntity){
    Optional<AccountEntity> sourceAccountOptional = accountRepository.findById(id);
    if (!sourceAccountOptional.isPresent()) {
      throw new IllegalArgumentException("Brak konta źródłowego w bazie danych");
    }
    AccountEntity sourceAccount = sourceAccountOptional.get();
    BigDecimal sourceAccountBalance = sourceAccount.getBalance();
    if(sourceAccountBalance.compareTo(operationEntity.getAmount()) < 0){
      throw new IllegalArgumentException("Brak środków na koncie");
    }
    return sourceAccount;
  }

  private AccountEntity verifyDestinationAccountExist(OperationEntity operationEntity){
    Optional<AccountEntity> destiantionAccount = accountRepository.findByNRB(operationEntity.getDestinationAccountNumber());
    if(!destiantionAccount.isPresent()){
      throw new IllegalArgumentException("Brak konta docelowego w bazie danych");
    }
    return destiantionAccount.get();
  }

  private void updateAccounts(AccountEntity sourceAccount, AccountEntity destinationAccount, BigDecimal amount){
    sourceAccount.subtractBalance(amount);
    destinationAccount.addBalance(amount);
    AccountEntity accountEntity = accountRepository.findById(sourceAccount.getId()).get();
    if (!sourceAccount.getVersion().equals(accountEntity.getVersion())) {
      throw new OptimisticLockException("Błąd edycji, odśwież dane.");
    }
    accountRepository.save(sourceAccount);
    accountEntity = accountRepository.findById(destinationAccount.getId()).get();
    if (!destinationAccount.getVersion().equals(accountEntity.getVersion())) {
      throw new OptimisticLockException("Błąd edycji, odśwież dane.");
    }
    accountRepository.save(destinationAccount);
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