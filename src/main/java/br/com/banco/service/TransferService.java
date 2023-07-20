package br.com.banco.service;

import br.com.banco.model.TransferEntity;
import br.com.banco.model.dto.TransferEntityDto;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface TransferService {
    @Transactional
    Optional<TransferEntityDto> insertTransfer(TransferEntity newTransfer);

    List<TransferEntityDto> listAllByAccountId(long accountId);

    List<TransferEntityDto> listAllByOriginName(String origin);

}
