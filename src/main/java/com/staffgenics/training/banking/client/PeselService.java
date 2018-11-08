package com.staffgenics.training.banking.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class PeselService {

  private final ClientRepository clientRepository;

  @Autowired
  PeselService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  void validatePesel(String pesel){
    //length
    if (pesel.length() != 11) {
      log.info("Budowa numeru PESEL nie jest poprawna.");
      throw new IllegalArgumentException("Budowa numeru PESEL nie jest poprawna.");
    }
    //allDigits and checksum
    boolean allDigits = true;
    int checkSum = 0;
    int[] weights = {1,3,7,9,1,3,7,9,1,3};

    for (int i = 0; i < pesel.length(); i++) {
      //all digits
      if (Character.isDigit(pesel.charAt(i))){
        allDigits = true;
      } else {
        log.info("Budowa numeru PESEL nie jest poprawna.");
        throw new IllegalArgumentException("Budowa numeru PESEL nie jest poprawna.");
      }
      //checksum
      if (i!=10) checkSum += (weights[i] * Character.getNumericValue(pesel.charAt(i))) % 10;
    }
    checkSum = 10 - (checkSum % 10);
    boolean isCheckSumValid = checkSum == Character.getNumericValue(pesel.charAt(pesel.length() - 1));
    if (!isCheckSumValid){
      log.info("Budowa numeru PESEL nie jest poprawna.");
      throw new IllegalArgumentException("Budowa numeru PESEL nie jest poprawna.");
    }
    log.info("Budowa numeru PESEL jest poprawna.");
  }

  String generatePeselForForeigner(Date birthDate, String name, String sureName){
    StringBuilder pesel = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
    pesel.append(sdf.format(birthDate).replace("/","")).append(name.substring(0,1)).append(sureName.substring(0,3));
    return pesel.toString();
  }
}
