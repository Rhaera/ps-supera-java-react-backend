package br.com.banco.model.dto;

import br.com.banco.model.TransferEntity.TransferTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class TransferEntityDto {

    private final Instant dateOfTransferOccurrence;

    private final BigDecimal amountTransferred;

    private final TransferTypes type;

    private String transferOrigin;

    private final Long transferAccountId;

}
