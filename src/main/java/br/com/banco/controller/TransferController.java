package br.com.banco.controller;

import br.com.banco.model.TransferEntity;
import br.com.banco.model.dto.TransferEntityDto;
import br.com.banco.service.TransferService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transfers")
@CrossOrigin(value = "**")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService service;

    @PostMapping(value = "/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Optional<TransferEntityDto> postTransfer(@RequestBody @Validated(value = TransferEntity.class) TransferEntity newTransfer) {
        return service.insertTransfer(newTransfer);
    }
    @GetMapping(value = "/{accountId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<TransferEntityDto> getAllByAccountId(@PathVariable(value = "accountId") long accountId) {
        return service.listAllByAccountId(accountId);
    }
    @GetMapping(value = "/{originName}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<TransferEntityDto> getAllByOriginName(@PathVariable(value = "originName") String origin) {
        return service.listAllByOriginName(origin);
    }
}
