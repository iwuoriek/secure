package com.example.secure.repository;

import com.example.secure.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByAccountNumber(long accountNumber);
    boolean existsByAccountNumber(long accountNumber);
}
