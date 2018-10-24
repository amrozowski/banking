package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący klientów.
 */
@Service
@Slf4j
class ClientService {

  private final ClientRepository clientRepository;

  @Autowired
  ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  List<ClientDto> getClients() {
    log.info("Pobieramy wszystkich klientów");
    return clientRepository.findAll().stream()
        .map(ClientDto::createInstance)
        .collect(Collectors.toList());
  }

  ClientDto getClient(Long id) {
    log.info("Pobieramy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    return ClientDto.createInstance(clientEntity);
  }

  private ClientEntity findClient(Long id) {
    Optional<ClientEntity> clientEntityOptional = clientRepository.findById(id);
    if (!clientEntityOptional.isPresent()) {
      throw new IllegalArgumentException("Brak klienta w bazie danych");
    }
    return clientEntityOptional.get();
  }

  Long createClient(ClientDto clientDto) {
    log.info("Dodajemy nowego klienta");
    ClientEntity clientEntity = ClientEntity.createInstance(clientDto);
    clientRepository.save(clientEntity);
    return clientEntity.getId();
  }

  void editClient(ClientDto clientDto, Long id) {
    log.info("Edytujemy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    clientEntity.update(clientDto);
    clientRepository.save(clientEntity);
  }
}
