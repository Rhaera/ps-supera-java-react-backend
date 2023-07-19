package br.com.banco.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "transferencia")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransferEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", table = "transferencia")
    private Long transferId;

    @NonNull
    @Column(name = "data_transferencia", nullable = false, table = "transferencia")
    private Instant dateOfTransferOccurrence;

    @NonNull
    @Column(name = "valor", nullable = false, table = "transferencia", precision = 20, scale = 2)
    private BigDecimal amountTransferred;

    @NonNull
    @Column(name = "tipo", nullable = false, table = "transferencia", length = 15)
    private TransferTypes type;

    @Column(name = "nome_operador_transacao", table = "transferencia", length = 50)
    private String transferRecipient;

    @NonNull
    @Column(name = "conta_id", table = "transferencia")
    private Long transferAccountId;

    public enum TransferTypes {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA
    }
}
