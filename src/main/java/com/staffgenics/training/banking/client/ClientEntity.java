package com.staffgenics.training.banking.client;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Encja z danymi klienta.
 */
@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
class ClientEntity {

  @Id
  private Long id;

  private String surname;

  private String name;

  private String pesel;
}
