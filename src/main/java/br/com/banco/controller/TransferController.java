package br.com.banco.controller;

import br.com.banco.service.TransferService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/transfers")
@CrossOrigin(value = "*")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

}
