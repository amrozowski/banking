package com.staffgenics.training.banking.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
interface CardRepository extends JpaRepository<CardEntity, Long> {

  @Query("SELECT card FROM CardEntity card WHERE card.cardNumber = :cardNumber")
  Optional<CardEntity> findCardByCardNumber(String cardNumber);

  @Query("SELECT card FROM CardEntity card WHERE card.accountId = :accountId AND card.cardType.code = :cardType")
  Optional<List<CardEntity>> findCardByAccountIdAndType(Long accountId, String cardType);

  @Query("SELECT card FROM CardEntity card WHERE card.validThruDate < :nowDate")
  Optional<List<CardEntity>> findExpiredCards(LocalDate nowDate);
}
