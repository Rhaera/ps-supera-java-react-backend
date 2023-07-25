package br.com.banco.service.implementation;

import br.com.banco.entity.TransferEntity;
import br.com.banco.dto.TransferDto;
import br.com.banco.repository.TransferRepository;
import br.com.banco.service.TransferService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepository repository;

    @Override
    public Optional<TransferDto> insertTransfer(TransferEntity newTransfer) {
        return repository.existsById(newTransfer.getTransferId()) ?
                Optional.empty() :
                Optional.of(repository.save(newTransfer).toDto());
    }
    @Override
    public List<TransferDto> listAllByAccountId(long accountId) {
        return repository.findAllByAccountId(accountId)
                .stream()
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<TransferDto> listAllByOriginNameAndAccountId(String origin, long accountId) {
        return repository.findAllByOrigin(origin)
                .stream()
                .filter(transferEntity -> transferEntity.getTransferAccount().getId() == accountId)
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<TransferDto> listAllTransfers() {
        return repository.findAll()
                .stream()
                .map(TransferEntity::toDto)
                .collect(Collectors.toList());
    }
}
