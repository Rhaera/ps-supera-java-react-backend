package br.com.banco.service.implementation;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;
import br.com.banco.repository.BankAccountRepository;
import br.com.banco.service.BankAccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;

    @Override
    public Optional<AccountDto> createNewAccount(BankAccount newAccount) {
        return repository.findOneByName(newAccount.getAccountOwnerName()).isEmpty() ? Optional.of(repository.save(newAccount).toDto()) : Optional.empty();
    }

    @Override
    public Optional<AccountDto> readAccountById(long id) {
        return repository.findById(id).isPresent() ? Optional.of(repository.findById(id).get().toDto()) : Optional.empty();
    }

    @Override
    public Optional<AccountDto> readAccountByName(String searchName) {
        return repository.findOneByName(searchName).isPresent() ? Optional.of(repository.findOneByName(searchName).get().toDto()) : Optional.empty();
    }
}
