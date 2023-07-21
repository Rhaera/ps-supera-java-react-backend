package br.com.banco.controller;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;
import br.com.banco.service.BankAccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/accounts")
@CrossOrigin(value = "**")
@RequiredArgsConstructor
public class AccountController {
    private final BankAccountService service;

    @PostMapping(value = "/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Optional<AccountDto> postAccount(@RequestParam(value = "newUser") String name) {
        return service.createNewAccount(new BankAccount(name));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<AccountDto> getAccountById(@PathVariable(value = "id") long id) {
        return service.readAccountById(id);
    }

    @GetMapping(value = "/{name}")
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<AccountDto> getAccountByName(@PathVariable(value = "name") String name) {
        return service.readAccountByName(name);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<AccountDto> putNewName(@PathVariable(value = "id") long existingId, @RequestParam(value = "newName") String newName) {
        return service.updateAccount(existingId, newName);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Optional<AccountDto> deleteAccount(@PathVariable(value = "id") long existingId) {
        if (service.readAccountById(existingId).isEmpty())
            return Optional.empty();
        AccountDto existingAccountDto = service.readAccountById(existingId).get();
        service.deleteAccountById(existingId);
        return Optional.of(existingAccountDto);
    }
}
