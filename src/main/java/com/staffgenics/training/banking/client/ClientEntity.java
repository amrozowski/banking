package com.staffgenics.training.banking.client;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    return clientEntity;
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
