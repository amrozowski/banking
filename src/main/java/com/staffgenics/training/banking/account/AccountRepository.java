package com.staffgenics.training.banking.account;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends JpaRepository<AccountEntity, Long> {
  @Query("SELECT account FROM AccountEntity account WHERE account.accountNumber = :accountNumber")
  Optional<AccountEntity> findByNRB(String accountNumber);
}
