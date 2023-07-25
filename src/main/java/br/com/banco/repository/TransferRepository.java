package br.com.banco.repository;

import br.com.banco.entity.TransferEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    @Query(value = "SELECT * FROM transferencia t WHERE t.conta_id = :accountId", nativeQuery = true)
    List<TransferEntity> findAllByAccountId(@Param(value = "accountId") long accId);

    @Query(value = "SELECT * FROM transferencia t WHERE t.nome_operador_transacao = :origin", nativeQuery = true)
    List<TransferEntity> findAllByOrigin(@Param(value = "origin") String originName);

    @Transactional @Modifying @Query(value = "DELETE FROM transferencia t WHERE t.conta_id = :accountId", nativeQuery = true)
    void deleteAllAccountTransfers(@Param(value = "accountId") long id);
}
