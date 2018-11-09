package com.staffgenics.training.banking.client;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Encja z danymi klienta.
 */
@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
public class ClientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String surname;

  private String name;

  private String pesel;

  private String secondName;

  private boolean vip;

  private boolean foreigner;

  private Date birthDate;

  @Version
  private Long version;

  static ClientEntity createInstance(ClientDto clientDto) {
    ClientEntity clientEntity = new ClientEntity();
    clientEntity.setSurname(clientDto.getSurname());
    clientEntity.setName(clientDto.getName());
    clientEntity.setPesel(clientDto.getPesel());
    clientEntity.setSecondName(clientDto.getSecondName());
    clientEntity.setVip(clientDto.isVip());
    clientEntity.setForeigner(clientDto.isForeigner());
    clientEntity.setBirthDate(clientDto.getBirthDate());
    clientEntity.validateClient();
    return clientEntity;
  }

  private void validateClient(){
    if(!foreigner){
      validatePesel();
    } else {
      generatePeselForForeigner();
    }
  }

  private void validatePesel(){
    //length
    if (pesel.length() != 11) {
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
        throw new IllegalArgumentException("Budowa numeru PESEL nie jest poprawna.");
      }
      //checksum
      if (i!=10) checkSum += (weights[i] * Character.getNumericValue(pesel.charAt(i))) % 10;
    }
    checkSum = 10 - (checkSum % 10);
    boolean isCheckSumValid = checkSum == Character.getNumericValue(pesel.charAt(pesel.length() - 1));
    if (!isCheckSumValid){
      throw new IllegalArgumentException("Budowa numeru PESEL nie jest poprawna.");
    }
  }

  private void generatePeselForForeigner(){
    StringBuilder foreignPesel = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy");
    foreignPesel.append(sdf.format(birthDate).replace("/","")).append(name.substring(0,1)).append(surname.substring(0,3));
    pesel = foreignPesel.toString();
  }

  void update(ClientDto clientDto) {
    setVersion(clientDto.getVersion());
    setSurname(clientDto.getSurname());
    setName(clientDto.getName());
    setPesel(clientDto.getPesel());
    setSecondName(clientDto.getSecondName());
    setVip(clientDto.isVip());
    setForeigner(clientDto.isForeigner());
    setBirthDate(clientDto.getBirthDate());
  }
}
