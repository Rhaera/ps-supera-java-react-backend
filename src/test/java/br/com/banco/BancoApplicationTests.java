package br.com.banco;

import br.com.banco.model.BankAccount;
import br.com.banco.model.TransferEntity;
import br.com.banco.model.TransferEntity.TransferTypes;
import br.com.banco.model.dto.AccountDto;
import br.com.banco.model.dto.TransferEntityDto;
import br.com.banco.repository.BankAccountRepository;
import br.com.banco.repository.TransferEntityRepository;
import br.com.banco.service.BankAccountService;
import br.com.banco.service.TransferService;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@TestPropertySource(value = "/application.properties")
@SpringBootTest(classes = BancoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BancoApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private BankAccountRepository accountsRepository;

    @Autowired
    private TransferEntityRepository transfersRepository;

    @Autowired
    private TransferService transferService;

    @Autowired
    private BankAccountService accountService;

    private String baseUrl = "http://localhost:";

    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        baseUrl = baseUrl.concat(port + "/");
        log.info("Application started with base URL: {} at port: {}", baseUrl, port);
        log.info("Initializing DB...");
    }
/*
    @AfterEach
    void eraseData() {
        long totalAccounts = accountsRepository.findAll().size();
        long totalTransfers = transfersRepository.findAll().size();
        log.info("Total Accounts in DB: {} With {} Transfers Made At All", totalAccounts, totalTransfers);
        log.info("Deleting All Data From DB.");
        accountsRepository.findAll().forEach(account -> transfersRepository.deleteAllAccountTransfers(account.getAccountId()));
        accountsRepository.deleteAll();
    }
*/
    @Test
    void checkingIfReadMethodsAreCorrectlyImplementedAtBothRepos() {

        assertInstanceOf(BankAccount.class, accountsRepository.findById(2L).orElseThrow(IllegalArgumentException::new));
        BankAccount genericAccountForTests = accountsRepository.findById(1L).isPresent() ? accountsRepository.findById(1L).get() : new BankAccount();
        assertAll(
            () -> assertNotNull(genericAccountForTests.getAccountId()),
            () -> assertEquals(1L, (long) genericAccountForTests.getAccountId()),
            () -> assertEquals(new BankAccount("Fulano").getAccountOwnerName(), genericAccountForTests.getAccountOwnerName())
        );

        assertInstanceOf(TransferEntity.class, transfersRepository.findById(5L).orElseThrow(IllegalArgumentException::new));
        TransferEntity genericTransferForTests = transfersRepository.findById(6L).isPresent() ? transfersRepository.findById(6L).get() : new TransferEntity();
        assertAll(
            () -> assertDoesNotThrow(genericTransferForTests::toDto),
            () -> assertNotNull(genericTransferForTests.getTransferId()),
            () -> assertTrue(genericTransferForTests.getDateOfTransferOccurrence().isBefore(Instant.now())),
            () -> assertEquals(BigDecimal.valueOf(25173.09), genericTransferForTests.getAmountTransferred()),
            () -> assertEquals(TransferTypes.TRANSFER, genericTransferForTests.toDto().getType()),
            () -> assertEquals("ronnyscley", genericTransferForTests.getTransferOrigin().toLowerCase()),
            () -> assertEquals(
                "Sicrano",
                accountsRepository.findById(genericTransferForTests.getTransferAccount().getAccountId())
                    .map(BankAccount::getAccountOwnerName)
                    .orElseThrow(IllegalArgumentException::new)
            )
        );
    }

    @Test
    void checkingIfCrudMethodsAreCorrectlyImplementedByBothServices() {

        assertInstanceOf(AccountDto.class, accountService.createNewAccount(new BankAccount("Test")).orElse(new AccountDto("Create Test Error")));
        AccountDto accountDtoForTests = accountService.readAccountById(3L).orElse(new AccountDto("Read Test Error"));
        assertAll(
            () -> assertFalse(accountService.readAccountsByName(accountDtoForTests.getName()).isEmpty()),
            () -> assertEquals("New Test", accountService.updateAccount(3L, "New Test").orElse(new AccountDto("Update Test Error")).getName()),
            () -> assertArrayEquals(Stream.of("Fulano", "Sicrano", "New Test").toArray(), accountService.readAllAccounts().stream().map(AccountDto::getName).toArray()),
            () -> assertDoesNotThrow(() -> accountService.deleteAccountById(accountService.readAllAccounts().size())),
            () -> assertTrue(accountService.readAccountsByName("New Test").isEmpty() && accountService.readAccountsByName("Delete Test Error").isEmpty()),
            () -> assertThrowsExactly(NullPointerException.class, () -> accountService.readAccountById(3L).orElseThrow(NullPointerException::new))
        );

        assertDoesNotThrow(() ->
            assertInstanceOf(
                TransferEntityDto.class,
                transferService.insertTransfer(
                    new TransferEntity(7L, Instant.now(), BigDecimal.valueOf(29.82), "TRANSFERENCIA", "Rhaera", new BankAccount(2L, "Sicrano"))
                ).orElseThrow(IllegalArgumentException::new)
            )
        );
        TransferEntityDto transferDtoForTests = transferService.listAllByAccountId(2L).get(3);
        assertAll(
            () -> assertFalse(transferService.listAllByOriginNameAndAccountId(transferDtoForTests.getTransferOrigin(), transferDtoForTests.getTransferAccountId()).isEmpty()),
            () -> assertEquals(7L, transferService.listAllTransfers().size()),
            () -> assertArrayEquals(
                Stream.of("Beltrano", "Ronnyscley", "Rhaera").toArray(),
                transferService.listAllTransfers()
                    .stream()
                    .map(TransferEntityDto::getTransferOrigin)
                    .filter(Objects::nonNull)
                    .toArray()
            ),
            () -> assertDoesNotThrow(() ->
                new TransferEntity(
                    transferDtoForTests.getDateOfTransferOccurrence(),
                    transferDtoForTests.getAmountTransferred(),
                    transferDtoForTests.getType().toString(),
                    new BankAccount(transferDtoForTests.getTransferAccountId(), "Sicrano")
                ).toDto()
            ),
            () -> assertNotNull(transferDtoForTests.getType())
        );
    }
}
