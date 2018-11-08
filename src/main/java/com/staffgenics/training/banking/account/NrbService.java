package com.staffgenics.training.banking.account;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NrbService {
  private final AccountRepository accountRepository;

  private static final int MAX_GENERATION_TRIES = 1000;

  @Autowired
  NrbService(AccountRepository accountRepository){this.accountRepository = accountRepository;}

  String generateUniqueNrb() {
    String accountNumber;
    int generationTry = 0;
    do {
      if (MAX_GENERATION_TRIES < generationTry) {
        throw new IllegalStateException("Błąd generacji konta, przekroczono maksymalną liczbę prób");
      }
      Iban iban = new Iban.Builder()
          .countryCode(CountryCode.PL)
          .bankCode("190")
          .buildRandom();
      accountNumber = iban.getBban();
      generationTry++;
    } while (checkNrbInRepository(accountNumber));
    return accountNumber;
  }
  private boolean checkNrbInRepository(String accountNumber){
    return accountRepository.findByNRB(accountNumber).isPresent();
  }

}
