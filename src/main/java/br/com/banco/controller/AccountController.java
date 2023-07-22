package br.com.banco.controller;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;
import br.com.banco.service.BankAccountService;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/accounts")
@CrossOrigin(value = "**")
@RequiredArgsConstructor
public class AccountController {

    private final BankAccountService service;

    @ApiOperation(
            value = "Inserting A New Bank Account Into Database",
            response = ResponseEntity.class,
            httpMethod = "POST",
            code = 201
    )
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<AccountDto> postAccount(@RequestParam(value = "newUser") String name) {
        return service.createNewAccount(new BankAccount(name)).map(accountCreated ->
            ResponseEntity.created(URI.create("/api/v1/accounts/" + service.readAllAccounts().size())).body(accountCreated)
        ).orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @ApiOperation(
            value = "Reading And Verifying If The Name Of A Bank Account From Database, Mapped By Path Variable Called \"id\", Exists",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(value = "id") long id) {
        return service.readAccountById(id).isEmpty() ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(service.readAccountById(id).get());
    }

    @ApiOperation(
            value = "Reading And Verifying All The Bank Accounts From Database Which Have The Same Name Mapped By Request Param Called \"name\"",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<AccountDto>> getAccountsByName(@RequestParam(value = "name") String name) {
        return service.readAccountsByName(name).isEmpty() ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(service.readAccountsByName(name));
    }

    @ApiOperation(
            value = "Reading And Verifying All The Bank Accounts From Database",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "/")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(service.readAllAccounts());
    }

    @ApiOperation(
            value = "Modifying Name Property Of The Bank Account From Database Mapped By Path Variable Called \"id\"",
            response = ResponseEntity.class,
            httpMethod = "PUT"
    )
    @PutMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<AccountDto> putNewName(@PathVariable(value = "id") long existingId, @RequestParam(value = "newName") String newName) {
        return service.readAccountById(existingId).isEmpty() ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(service.updateAccount(existingId, newName).orElse(new AccountDto(newName)));
    }

    @ApiOperation(
            value = "Deleting The Bank Account From Database Which Have The Same ID Mapped By Path Variable Called \"id\"",
            response = ResponseEntity.class,
            httpMethod = "DELETE"
    )
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<AccountDto> deleteAccount(@PathVariable(value = "id") long existingId) {
        if (service.readAccountById(existingId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AccountDto existingAccountDto = service.readAccountById(existingId).get();
        service.deleteAccountById(existingId);
        return ResponseEntity.ok(existingAccountDto);
    }
}
