package br.com.banco.service.implementation;

import br.com.banco.model.TransferEntity;
import br.com.banco.model.dto.TransferEntityDto;
import br.com.banco.repository.TransferEntityRepository;
import br.com.banco.service.TransferService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferEntityRepository repository;

    @Override
    public Optional<TransferEntityDto> insertTransfer(TransferEntity newTransfer) {
        return repository.existsById(newTransfer.getTransferId()) ?
                Optional.empty() :
                Optional.of(repository.save(newTransfer).toDto());
    }
    @Override
    public List<TransferEntityDto> listAllByAccountId(long accountId) {
        return repository.findAllByAccountId(accountId)
                .stream()
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<TransferEntityDto> listAllByOriginNameAndAccountId(String origin, long accountId) {
        return repository.findAllByOrigin(origin)
                .stream()
                .filter(transferEntity -> transferEntity.getTransferAccount().getAccountId() == accountId)
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<TransferEntityDto> listAllTransfers() {
        return repository.findAll()
                .stream()
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
}
