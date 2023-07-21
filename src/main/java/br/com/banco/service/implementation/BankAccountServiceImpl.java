package br.com.banco.service.implementation;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;
import br.com.banco.repository.BankAccountRepository;
import br.com.banco.repository.TransferEntityRepository;
import br.com.banco.service.BankAccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final TransferEntityRepository transferRepository;

    @Override
    public Optional<AccountDto> createNewAccount(BankAccount newAccount) {
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
                .map(BankAccount::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<AccountDto> readAllAccounts() {
        return repository.findAll()
                .stream()
                .map(BankAccount::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<AccountDto> updateAccount(long existingId, String newName) {
        if (repository.findById(existingId).isEmpty())
            return Optional.empty();
        return Optional.of(repository.save(new BankAccount(repository.findById(existingId).get().getAccountId(), newName)).toDto());
    }
    @Override
    public void deleteAccountById(long id) {
        if (repository.findById(id).isEmpty())
            return;
        transferRepository.deleteAllAccountTransfers(id);
        repository.deleteById(id);
    }
}
