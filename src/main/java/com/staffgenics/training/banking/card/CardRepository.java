package com.staffgenics.training.banking.card;

import com.staffgenics.training.banking.client.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {

  @Query("SELECT card FROM CardEntity card WHERE card.cardNumber = :cardNumber")
  Optional<CardEntity> findCardByCardNumber(String cardNumber);

  @Query("SELECT card FROM CardEntity card WHERE card.accountId = :accountId")
  Optional<CardEntity> findCardByAccountId(Long accountId);
}
