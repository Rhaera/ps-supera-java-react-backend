package br.com.banco.controller;

import br.com.banco.service.BankAccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/accounts")
@CrossOrigin(value = "*")
@RequiredArgsConstructor
public class AccountController {

    private final BankAccountService service;

}
