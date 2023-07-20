package br.com.banco.model.dto;

import br.com.banco.model.TransferEntity.TransferTypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Getter
public class TransferEntityDto {
    private final Instant dateOfTransferOccurrence;

    private final BigDecimal amountTransferred;

    private final TransferTypes type;

    private final String transferOrigin;

    private final Long transferAccountId;

}
