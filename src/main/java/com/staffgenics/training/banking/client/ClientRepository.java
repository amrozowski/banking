package com.staffgenics.training.banking.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
      "WHERE client.surname = :surname " +
      "AND (client.name = :name OR :name = NULL OR :name = '') " +
      "AND (client.secondName = :secondName OR :secondName = NULL OR :secondName = '')")
  //Zapytac o @Param
  List<ClientEntity> findClientByCriteria(String name, String surname, String secondName);
}
