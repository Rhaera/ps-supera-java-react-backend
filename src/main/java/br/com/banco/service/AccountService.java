package br.com.banco.service;

import br.com.banco.entity.AccountEntity;
import br.com.banco.dto.AccountDto;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface AccountService {
    Optional<AccountDto> createNewAccount(AccountEntity newAccount);
    Optional<AccountDto> readAccountById(long id);
    List<AccountDto> readAccountsByName(String searchName);
    List<AccountDto> readAllAccounts();
    Optional<AccountDto> updateAccount(long existingId, String newName);
    Optional<AccountDto> deleteAccountById(long id);
}
