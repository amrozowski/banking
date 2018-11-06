package com.staffgenics.training.banking.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium klientów.
 */
@Repository
interface ClientRepository extends JpaRepository<ClientEntity, Long> {

  @Query("SELECT client FROM ClientEntity client WHERE client.pesel = :pesel")
  Optional<ClientEntity> findClientByPesel(String pesel);

  @Query("SELECT client FROM ClientEntity client " +
      "WHERE client.name = :name " +
      "AND client.surname = :surname " +
      "AND client.secondName = :secondName")
  List<ClientEntity> findClientByCriteria(String name, String surname, String secondName);

  @Query("SELECT client FROM ClientEntity client " +
      "WHERE client.name = :name " +
      "AND client.surname = :surname ")
  List<ClientEntity> findClientByCriteria(String name, String surname);

  @Query("SELECT client FROM ClientEntity client WHERE client.surname = :surname")
  List<ClientEntity> findClientByCriteria(String surname);

}
