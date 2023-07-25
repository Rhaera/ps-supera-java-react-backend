package br.com.banco.service.implementation;

import br.com.banco.entity.AccountEntity;
import br.com.banco.dto.AccountDto;
import br.com.banco.repository.AccountRepository;
import br.com.banco.repository.TransferRepository;
import br.com.banco.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final TransferRepository transferRepository;
    @Override
    public Optional<AccountDto> createNewAccount(AccountEntity newAccount) {
        return Optional.of(repository.save(newAccount).toDto());
    }
    @Override
    public Optional<AccountDto> readAccountById(long id) {
        return repository.findById(id).isPresent() ? Optional.of(repository.findById(id).get().toDto()) : Optional.empty();
    }
    @Override
    public List<AccountDto> readAccountsByName(String searchName) {
        return repository.findAllByName(searchName)
                .stream()
                .map(AccountEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<AccountDto> readAllAccounts() {
        return repository.findAll()
                .stream()
                .map(AccountEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<AccountDto> updateAccount(long existingId, String newName) {
        if (repository.findById(existingId).isEmpty())
            return Optional.empty();
        return Optional.of(repository.save(new AccountEntity(repository.findById(existingId).get().getId(), newName)).toDto());
    }
    @Override
    public Optional<AccountDto> deleteAccountById(long id) {
        if (repository.findById(id).isEmpty())
            return Optional.empty();
        AccountEntity existingAccount = repository.findById(id).get();
        transferRepository.deleteAllAccountTransfers(id);
        repository.deleteById(id);
        return Optional.of(existingAccount.toDto());
    }
}
