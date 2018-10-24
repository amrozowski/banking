package com.staffgenics.training.banking.client;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja z danymi klienta.
 */
@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class ClientEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String surname;

  private String name;

  private String pesel;

  private String secondName;

  private boolean vip;

  static ClientEntity createInstance(ClientDto clientDto) {
    ClientEntity clientEntity = new ClientEntity();
    clientEntity.setSurname(clientDto.getSurname());
    clientEntity.setName(clientDto.getName());
    clientEntity.setPesel(clientDto.getPesel());
    clientEntity.setSecondName(clientDto.getSecondName());
    clientEntity.setVip(clientDto.isVip());
    return clientEntity;
  }

  void update(ClientDto clientDto) {
    setSurname(clientDto.getSurname());
    setName(clientDto.getName());
    setPesel(clientDto.getPesel());
    setSecondName(clientDto.getSecondName());
    setVip(clientDto.isVip());
  }
}
