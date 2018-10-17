package com.staffgenics.training.banking.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  static ClientDto createInstance(ClientEntity clientEntity) {
    ClientDto clientDto = new ClientDto();
    clientDto.setId(clientEntity.getId());
    clientDto.setSurname(clientEntity.getSurname());
    clientDto.setName(clientEntity.getName());
    clientDto.setPesel(clientEntity.getPesel());
    return clientDto;
  }
}
