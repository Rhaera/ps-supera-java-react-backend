package br.com.banco.repository;

import br.com.banco.model.BankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query(value = "SELECT * FROM conta c WHERE c.nome_responsavel = :queriedName", nativeQuery = true)
    List<BankAccount> findAllByName(@Param(value = "queriedName") String name);

}
