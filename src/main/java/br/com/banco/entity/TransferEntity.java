package br.com.banco.entity;

import br.com.banco.dto.TransferDto;

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
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String type;

    @Column(name = "nome_operador_transacao", table = "transferencia", length = 50)
    private String transferOrigin;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AccountEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "conta_id", referencedColumnName = "id_conta", nullable = false)
    private AccountEntity transferAccount;

    public enum TransferTypes {
        DEPOSIT("DEPOSITO"),
        WITHDRAWAL("SAQUE"),
        TRANSFER("TRANSFERENCIA");

        private final String type;

        TransferTypes(String transferType) {
            this.type = transferType;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public TransferDto toDto() throws IllegalArgumentException {
        switch (type) {
            case "DEPOSITO":
                return new TransferDto(dateOfTransferOccurrence, amountTransferred, TransferTypes.DEPOSIT, transferOrigin, transferAccount.getId());
            case "SAQUE":
                return new TransferDto(dateOfTransferOccurrence, amountTransferred, TransferTypes.WITHDRAWAL, transferOrigin, transferAccount.getId());
            case "TRANSFERENCIA":
                return new TransferDto(dateOfTransferOccurrence, amountTransferred, TransferTypes.TRANSFER, transferOrigin, transferAccount.getId());
            default:
                throw new IllegalArgumentException("FORBIDDEN OPERATION!");
        }
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
            Short.hashCode((short) dateOfTransferOccurrence.toEpochMilli())) *
            Short.hashCode((amountTransferred.shortValue())));
    }
}
