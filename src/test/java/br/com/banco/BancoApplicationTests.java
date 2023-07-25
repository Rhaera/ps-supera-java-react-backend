package br.com.banco;

import br.com.banco.entity.AccountEntity;
import br.com.banco.entity.TransferEntity;
import br.com.banco.entity.TransferEntity.TransferTypes;
import br.com.banco.dto.AccountDto;
import br.com.banco.dto.TransferDto;
import br.com.banco.repository.AccountRepository;
import br.com.banco.repository.TransferRepository;
import br.com.banco.service.AccountService;
import br.com.banco.service.TransferService;

import lombok.extern.slf4j.Slf4j;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@TestPropertySource(value = "/application.properties")
@SpringBootTest(classes = BancoApplication.class)
class BancoApplicationTests {

    @Autowired
    private AccountRepository accountsRepository;
    @Autowired
    private TransferRepository transfersRepository;
    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountService accountService;
    private int counter;

    @AfterEach
    void eraseData() {
        long totalAccounts = accountsRepository.findAll().size();
        long totalTransfers = transfersRepository.findAll().size();
        log.info("Total Accounts in DB: {} With {} Transfers Made At All", totalAccounts, totalTransfers);
        if (counter == 1) {
            log.info("Deleting All Data From DB.");
            accountsRepository.findAll().forEach(account -> transfersRepository.deleteAllAccountTransfers(account.getId()));
            accountsRepository.deleteAll();
        }
    }
    @Test
    @DisplayName(value = "REPOSITORY TEST")
    void checkingIfReadMethodsAreCorrectlyImplementedAtBothRepos() {
        counter = 1;

        assertInstanceOf(AccountEntity.class, accountsRepository.findById(2L).orElseThrow(IllegalArgumentException::new));
        AccountEntity genericAccountForTests = accountsRepository.findById(1L).isPresent() ? accountsRepository.findById(1L).get() : new AccountEntity();
        assertAll(
            () -> assertNotNull(genericAccountForTests.getId()),
            () -> assertEquals(1L, (long) genericAccountForTests.getId()),
            () -> assertEquals(new AccountEntity("Fulano").getAccountOwnerName(), genericAccountForTests.getAccountOwnerName())
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
                accountsRepository.findById(genericTransferForTests.getTransferAccount().getId())
                    .map(AccountEntity::getAccountOwnerName)
                    .orElseThrow(IllegalArgumentException::new)
            )
        );
    }
    @Test
    @DisplayName(value = "SERVICE TEST")
    void checkingIfCrudMethodsAreCorrectlyImplementedByBothServices() {
        counter = 0;

        assertInstanceOf(AccountDto.class, accountService.createNewAccount(new AccountEntity("Test")).orElse(new AccountDto("Create Test Error")));
        AccountDto accountDtoForTests = accountService.readAccountById(3L).orElse(new AccountDto("Read Test Error"));
        assertAll(
            () -> assertFalse(accountService.readAccountsByName(accountDtoForTests.getName()).isEmpty()),
            () -> assertEquals("New Test", accountService.updateAccount(3L, "New Test").orElse(new AccountDto("Update Test Error")).getName()),
            () -> assertArrayEquals(Stream.of("Fulano", "Sicrano", "New Test").toArray(), accountService.readAllAccounts().stream().map(AccountDto::getName).toArray()),
            () -> assertDoesNotThrow(() -> accountService.deleteAccountById(accountService.readAllAccounts().size())),
            () -> assertTrue(accountService.readAccountsByName("New Test").isEmpty() || !accountService.readAccountsByName("Delete Test Error").isEmpty()),
            () -> assertThrowsExactly(NullPointerException.class, () -> accountService.readAccountById(3L).orElseThrow(NullPointerException::new))
        );

        assertDoesNotThrow(() ->
            assertInstanceOf(
                TransferDto.class,
                transferService.insertTransfer(
                    new TransferEntity(7L, Instant.now(), BigDecimal.valueOf(29.82), "TRANSFERENCIA", "Rhaera", new AccountEntity(2L, "Sicrano"))
                ).orElseThrow(IllegalArgumentException::new)
            )
        );
        TransferDto transferDtoForTests = transferService.listAllByAccountId(2L).get(3);
        assertAll(
            () -> assertFalse(transferService.listAllByOriginNameAndAccountId(transferDtoForTests.getTransferOrigin(), transferDtoForTests.getTransferAccountId()).isEmpty()),
            () -> assertEquals(7L, transferService.listAllTransfers().size()),
            () -> assertArrayEquals(
                Stream.of("Beltrano", "Ronnyscley", "Rhaera").toArray(),
                transferService.listAllTransfers()
                    .stream()
                    .map(TransferDto::getTransferOrigin)
                    .filter(Objects::nonNull)
                    .toArray()
            ),
            () -> assertDoesNotThrow(() ->
                new TransferEntity(
                    transferDtoForTests.getDateOfTransferOccurrence(),
                    transferDtoForTests.getAmountTransferred(),
                    transferDtoForTests.getType().toString(),
                    new AccountEntity(transferDtoForTests.getTransferAccountId(), "Sicrano")
                ).toDto()
            ),
            () -> assertNotNull(transferDtoForTests.getType())
        );
    }
}
