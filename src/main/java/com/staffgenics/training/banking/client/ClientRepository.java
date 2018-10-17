package com.staffgenics.training.banking.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium klientów.
 */
@Repository
interface ClientRepository extends JpaRepository<ClientEntity, Long> {

}
