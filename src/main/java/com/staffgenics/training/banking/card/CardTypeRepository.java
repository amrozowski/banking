package com.staffgenics.training.banking.card;

import com.staffgenics.training.banking.account.AccountEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CardTypeRepository extends JpaRepository<CardTypeEntity, String> {

  @Cacheable("AccountRepository.findAll")
  @Override
  List<CardTypeEntity> findAll();

  @CacheEvict(value = "AccountRepository.findAll", allEntries = true)
  @Override
  <S extends CardTypeEntity> S save(S entity);

  @Query("SELECT cardType FROM CardTypeEntity cardType WHERE cardType.code = :code")
  Optional<CardTypeEntity> findByCode(String code);
}
