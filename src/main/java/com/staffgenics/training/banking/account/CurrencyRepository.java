package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium słownika walut.
 */
@Repository
interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

}
