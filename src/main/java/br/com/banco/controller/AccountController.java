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
}
