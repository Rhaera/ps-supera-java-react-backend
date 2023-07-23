package br.com.banco;

import br.com.banco.model.BankAccount;
import br.com.banco.model.dto.AccountDto;

import br.com.banco.model.dto.TransferEntityDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestPropertySource(value = "/application.properties")
@SpringBootTest(classes = BancoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BancoApplicationControllerTests {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;

    private String baseUrl = "http://localhost:";

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        baseUrl = baseUrl.concat(port + "/");
        log.info("Application started with base URL: {} at port: {}", baseUrl, port);
        log.info("Initializing DB...");
    }

    @Test
    @DisplayName(value = "CONTROLLER TEST")
    @Sql(scripts = "classpath:/initialize-data.sql")
    void checkingIfRestApiHttpMethodsAreCorrectlyImplementedByBothControllers() throws JSONException, JsonProcessingException {

        assertDoesNotThrow(() ->
            restTemplate.put(
                URI.create(baseUrl.concat("api/v1/accounts/5?newName=NextJs")),
                new AccountDto("PUT Method Test Error")
            )
        );

        String stringAccountDto = restTemplate.getForObject(URI.create(baseUrl.concat("api/v1/accounts/5")), String.class);
        assert stringAccountDto != null;
        stringAccountDto = stringAccountDto.substring(stringAccountDto.indexOf(":"), stringAccountDto.lastIndexOf("}")).substring(2);

        assertEquals(new AccountDto("NextJs").getName(), stringAccountDto.substring(0, stringAccountDto.length() - 1));

        String stringTransferDto = restTemplate.postForObject(
                baseUrl.concat("/api/v1/transfers"),
                new HttpEntity<>(
                        new JSONObject().put("amountTransferred", BigDecimal.valueOf(0.99))
                                .put("dateOfTransferOccurrence", Instant.now().minusSeconds(5L))
                                .put("transferAccount", new BankAccount(4L, "Angular"))
                                .put("transferId", 12L)
                                .put("transferOrigin", "Fulano")
                                .put("type", "TRANSFERENCIA")
                                .toString(),
                        httpHeaders
                ),
                String.class
        );
        JsonNode jsonNodeTransferDto = new ObjectMapper().readTree(stringTransferDto);

        assertNotNull(
            jsonNodeTransferDto
        );

        String regex = '}' + "," + '*';
        assertEquals(12, Objects.requireNonNull(restTemplate.getForObject(URI.create(baseUrl.concat("/api/v1/transfers/")), String.class)).split(regex).length);

        assertEquals(2, Objects.requireNonNull(restTemplate.getForObject(URI.create(baseUrl.concat("/api/v1/transfers?originName=Fulano&id=4")), String.class)).split(regex).length);

        assertEquals(3, Objects.requireNonNull(restTemplate.getForObject(URI.create(baseUrl.concat("/api/v1/transfers/4")), String.class)).split(regex).length);

        assertDoesNotThrow(() -> {
            restTemplate.postForLocation(URI.create(baseUrl.concat("api/v1/accounts?newUser=Redux")), new AccountDto("POST Method Test Error"));
            restTemplate.delete(URI.create(baseUrl.concat("api/v1/accounts/4")));
            restTemplate.delete(URI.create(baseUrl.concat("api/v1/accounts/5")));
        });

        assertNotNull(restTemplate.getForObject(URI.create(baseUrl.concat("api/v1/accounts/6")), String.class));
        assertFalse(
            Objects.requireNonNull(restTemplate.getForObject(URI.create(baseUrl.concat("api/v1/accounts?name=Redux")), String.class)).isEmpty() ||
            !Objects.requireNonNull(restTemplate.getForObject(URI.create(baseUrl.concat("api/v1/accounts/")), String.class)).contains("Redux")
        );

        restTemplate.delete(URI.create(baseUrl.concat("api/v1/accounts/6")));

        assertNotNull(restTemplate.getForObject(URI.create(baseUrl.concat("/api/v1/transfers/")), String.class));
    }
}
