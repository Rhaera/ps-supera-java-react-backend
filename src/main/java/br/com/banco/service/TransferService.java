package br.com.banco.service;

import br.com.banco.entity.TransferEntity;
import br.com.banco.dto.TransferDto;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface TransferService {

    @Transactional
    Optional<TransferDto> insertTransfer(TransferEntity newTransfer);
    List<TransferDto> listAllByAccountId(long accountId);
    List<TransferDto> listAllByOriginNameAndAccountId(String origin, long accountId);
    List<TransferDto> listAllTransfers();

}
