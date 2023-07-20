package br.com.banco.model;

import br.com.banco.model.dto.AccountDto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "conta")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
public class BankAccount {
    @Id
    @GeneratedValue
    @Column(name = "id_conta", table = "conta")
    private Long accountId;

    @NonNull
    @Column(name = "nome_responsavel", nullable = false, table = "conta", length = 50)
    private String accountOwnerName;

    public AccountDto toDto() {
        return new AccountDto(accountOwnerName);
    }
}
