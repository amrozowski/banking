package com.staffgenics.training.banking.account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.staffgenics.training.banking.exception.NotFoundException;
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
    CurrencyEntity currencyEntity = currencyRepository.getOne(accountDto.getCurrency());
    AccountEntity accountEntity = AccountEntity.createInstance(accountDto, currencyEntity);
    accountEntity.setAccountNumber(nrbService.generateUniqueNrb());
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
    operationEntity.setSourceAccountId(id);
    AccountEntity sourceAccount = verifyAccountExists(id);
    AccountEntity destinationAccount = verifyAccountExist(operationEntity.getDestinationAccountNumber());
    updateAccounts(sourceAccount, destinationAccount, operationEntity.getAmount());
    operationService.addOperation(operationEntity);
    return operationEntity.getId();
  }

  private AccountEntity verifyAccountExists(Long id){
    Optional<AccountEntity> accountEnitityOptional = accountRepository.findById(id);
    if (!accountEnitityOptional.isPresent()) {
      throw new NotFoundException("Brak konta w bazie danych");
    }
    return accountEnitityOptional.get();
  }

  private AccountEntity verifyAccountExist(String accountNumber){
    Optional<AccountEntity> accountEnitityOptional = accountRepository.findByNRB(accountNumber);
    if(!accountEnitityOptional.isPresent()){
      throw new NotFoundException("Brak konta w bazie danych");
    }
    return accountEnitityOptional.get();
  }

  private void updateAccounts(AccountEntity sourceAccount, AccountEntity destinationAccount, BigDecimal amount){
    String sourceCurrency = sourceAccount.getCurrency().getCode();
    String destinationCurrency = destinationAccount.getCurrency().getCode();
    if(sourceCurrency.equals(destinationCurrency)) {
      sourceAccount.subtractBalance(amount);
      destinationAccount.addBalance(amount);
    } else if(sourceCurrency.equals("PLN")){
      sourceAccount.subtractBalance(amount);
      destinationAccount.addBalance(exchangePlnAmountToOtherCurrency(amount, destinationCurrency));
    } else if(destinationCurrency.equals("PLN")){
      sourceAccount.subtractBalance(amount);
      destinationAccount.addBalance(exchangeAmountToPLN(sourceCurrency, amount));
    }
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

  private BigDecimal exchangeAmountToPLN(String currency, BigDecimal amount){
    BigDecimal exchangeRate = currencyRepository.getOne(currency).getExchangeRate();
    return amount.multiply(exchangeRate);
  }

  private BigDecimal exchangePlnAmountToOtherCurrency(BigDecimal amount, String currency){
    BigDecimal exchangeRate = currencyRepository.getOne(currency).getExchangeRate();
    return amount.divide(exchangeRate);
  }

  List<OperationDto> findOperations(SearchOperationDto searchOperationDto, Long id){
    List<OperationDto> operationList;
    String nrb = accountRepository.findById(id).get().getAccountNumber();
    if("IN".equals(searchOperationDto.getOperationType())){
      operationList = operationRepository.findIncomeByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), nrb).stream()
          .map(OperationDto::createInstance)
          .collect(Collectors.toList());
    } else if("OUT".equals(searchOperationDto.getOperationType())){
      operationList = operationRepository.findOutByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), id).stream()
          .map(OperationDto::createInstance)
          .collect(Collectors.toList());
    } else {
      operationList = operationRepository.findIncomeByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), nrb).stream()
          .map(OperationDto::createInstance)
          .collect(Collectors.toList());
      operationList.addAll(operationRepository.findOutByAmountAndDate(searchOperationDto.getAmountFrom(), searchOperationDto.getAmountTo(), searchOperationDto.getDateFrom(), searchOperationDto.getDateTo(), id).stream()
          .map(OperationDto::createInstance)
          .collect(Collectors.toList()));
    }
    return operationList;
  }

  public void deleteAccountsByClientId(Long clientId) {
    List<AccountEntity> accountEntityList = accountRepository.findAccountsByClientId(clientId);
    for (AccountEntity accountEntity: accountEntityList) {
      accountRepository.delete(accountEntity);
    }
  }
  public void setAccountsFlagDeleted(Long clientId) {
    List<AccountEntity> accountEntityList = accountRepository.findAccountsByClientId(clientId);
    for (AccountEntity accountEntity: accountEntityList) {
      accountEntity.setDeleted(true);
    }
    accountRepository.saveAll(accountEntityList);
  }
}