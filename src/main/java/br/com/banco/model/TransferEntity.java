package br.com.banco.model;

import br.com.banco.model.dto.TransferEntityDto;

import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

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
    private String transferOrigin;

    @NonNull
    @ManyToOne(targetEntity = BankAccount.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_conta")
    private Long transferAccountId;

    public enum TransferTypes {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA
    }

    public TransferEntityDto toDto() {
        return new TransferEntityDto(dateOfTransferOccurrence, amountTransferred, type, transferOrigin, transferAccountId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TransferEntity))
            return false;
        TransferEntity entityObj = (TransferEntity) obj;
        return Objects.equals(this.transferId, entityObj.transferId) && this.dateOfTransferOccurrence == entityObj.dateOfTransferOccurrence;
    }

    @Override
    public int hashCode() {
        return (int) (31 * ((Objects.nonNull(transferId) ?
            (transferId << 5) - transferId :
            (dateOfTransferOccurrence.hashCode() << 5) - dateOfTransferOccurrence.hashCode()) *
            Short.hashCode((short) dateOfTransferOccurrence.toEpochMilli())) * Short.hashCode((amountTransferred.shortValue())));
    }
}
