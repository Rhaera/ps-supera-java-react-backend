package br.com.banco.service;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;

import javax.transaction.Transactional;

import java.util.Optional;

public interface BankAccountService {

    @Transactional
    Optional<AccountDto> createNewAccount(BankAccount newAccount);

    Optional<AccountDto> readAccountById(long id);

    Optional<AccountDto> readAccountByName(String searchName);

}
