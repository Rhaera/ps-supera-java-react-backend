package br.com.banco.controller;

import br.com.banco.model.TransferEntity;
import br.com.banco.model.dto.TransferEntityDto;
import br.com.banco.service.TransferService;

import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/transfers")
@CrossOrigin(value = "**")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService service;

    @ApiOperation(
            value = "Inserting A New Transfer Into Database, With Validation",
            response = ResponseEntity.class,
            httpMethod = "POST",
            code = 201
    )
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<TransferEntityDto> postTransfer(@RequestBody @Validated(value = TransferEntity.class) TransferEntity newTransfer) {
        try {
            newTransfer.toDto();
        } catch (IllegalArgumentException forbiddenOperation) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return service.insertTransfer(newTransfer).map(transferMade ->
            ResponseEntity.created(URI.create("/api/v1/transfers/".concat(
                    transferMade.getTransferAccountId() + "/" +
                        service.listAllByAccountId(transferMade.getTransferAccountId()).size() + "/all-transfers/" +
                        service.listAllTransfers().size()
                    )
                )
            ).body(transferMade)
        ).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
    @ApiOperation(
            value = "Reading And Verifying How Many Transfers Were Made By The Account Mapped By Path Variable Called \"accountId\"",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "/{accountId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<TransferEntityDto>> getAllByAccountId(@PathVariable(value = "accountId") long accountId) {
        List<TransferEntityDto> accountTransfers = service.listAllByAccountId(accountId);
        return accountTransfers.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(accountTransfers);
    }
    @ApiOperation(
            value = "Reading And Verifying How Many Transfers Were Made By The Account Mapped By Request Parameters Called \"originName\" And \"accountId\"",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<TransferEntityDto>> getAllByOriginName(@RequestParam(value = "originName", required = false, defaultValue = "") String origin, @RequestParam(value = "id") long accountId) {
        List<TransferEntityDto> accountTransfersByOrigin = Objects.nonNull(origin) && !origin.isEmpty() ?
                service.listAllByOriginNameAndAccountId(origin, accountId) :
                service.listAllByAccountId(accountId);
        return accountTransfersByOrigin.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(accountTransfersByOrigin);
    }
    @ApiOperation(
            value = "Reading All Transfers Were Made",
            response = ResponseEntity.class,
            httpMethod = "GET"
    )
    @GetMapping(value = "/")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<TransferEntityDto>> getAllTransfers() {
        return ResponseEntity.ok(service.listAllTransfers());
    }
}
