package com.staffgenics.training.banking.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends JpaRepository<AccountEntity, Long> {

}
