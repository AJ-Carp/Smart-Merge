package com.smartmerge.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.smartmerge.model.Account;
import com.smartmerge.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;

    public Optional<Account> findByUserId(long id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
