package br.com.banco.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class AccountDto implements Serializable {

    @JsonProperty(value = "name", required = true)
    private final String name;

}
