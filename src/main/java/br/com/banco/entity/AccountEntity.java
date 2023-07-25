package br.com.banco.entity;

import br.com.banco.dto.AccountDto;

import lombok.*;

import javax.persistence.*;

@Entity(name = "conta")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta", table = "conta")
    private Long id;

    @NonNull
    @Column(name = "nome_responsavel", nullable = false, table = "conta", length = 50)
    private String accountOwnerName;

    public AccountDto toDto() {
        return new AccountDto(accountOwnerName);
    }
}
