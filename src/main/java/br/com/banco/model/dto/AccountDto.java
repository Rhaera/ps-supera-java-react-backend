package br.com.banco.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDto {

    private static final AccountDto singletonAccount = new AccountDto();

    private String name;

    public static AccountDto getInstance(String name) {
        singletonAccount.name = name;
        return singletonAccount;
    }
}
