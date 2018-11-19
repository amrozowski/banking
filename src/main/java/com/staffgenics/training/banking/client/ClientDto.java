package com.staffgenics.training.banking.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO z danymi klienta.
 */
@NoArgsConstructor
@Getter
@Setter
public class ClientDto {

  private Long id;

  private String surname;

  private String name;

  private String pesel;

  private String secondName;

  private boolean vip;

  private boolean foreigner;

  private Date birthDate;

  private Long version;

  private boolean deleted;

  static ClientDto createInstance(ClientEntity clientEntity) {
    ClientDto clientDto = new ClientDto();
    clientDto.setId(clientEntity.getId());
    clientDto.setSurname(clientEntity.getSurname());
    clientDto.setName(clientEntity.getName());
    clientDto.setPesel(clientEntity.getPesel());
    clientDto.setSecondName(clientEntity.getSecondName());
    clientDto.setVip(clientEntity.isVip());
    clientDto.setForeigner(clientEntity.isForeigner());
    clientDto.setBirthDate(clientEntity.getBirthDate());
    clientDto.setDeleted(clientEntity.isDeleted());
    return clientDto;
  }
}
