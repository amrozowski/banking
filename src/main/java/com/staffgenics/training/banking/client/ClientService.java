package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.staffgenics.training.banking.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

/**
 * Serwis obsługujący klientów.
 */
@Service
@Slf4j
class ClientService {

  private final ClientRepository clientRepository;
  private final AccountService accountService;

  @Autowired
  ClientService(ClientRepository clientRepository, AccountService accountService) {

    this.clientRepository = clientRepository;
    this.accountService = accountService;
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
    verifyClientExists(clientEntity);
    clientRepository.save(clientEntity);
    return clientEntity.getId();
  }

  void verifyClientExists(ClientEntity clientEntity) {
    Optional<ClientEntity> clientEntityOptional = clientRepository.findClientByPesel(clientEntity.getPesel());
    if(clientEntityOptional.isPresent()) {
      ClientEntity clientEntityDB = clientEntityOptional.get();
      if (clientEntity.getName().equals(clientEntityDB.getName()) && clientEntity.getSecondName().equals(clientEntityDB.getSecondName()) &&
          clientEntity.getSurname().equals(clientEntityDB.getSurname())) {
        throw new IllegalArgumentException("Klient o podanym numerze PESEL istnieje - dane osobowe zgodne");
      } else{
        throw new IllegalArgumentException("Klient o podanym numerze PESEL istnieje - dane osobowe niezgodne");
      }
    }
  }

  void editClient(ClientDto clientDto, Long id) {
    log.info("Edytujemy klienta o id: {}", id);
    ClientEntity clientEntity = findClient(id);
    if (!clientEntity.getVersion().equals(clientDto.getVersion())) {
      throw new OptimisticLockException("Błąd edycji, odśwież dane.");
    }
    clientEntity.update(clientDto);
    clientRepository.save(clientEntity);
  }

  List<ClientDto> getClientByCriteria(SearchClientDto searchClientDto){
    return clientRepository.findClientByCriteria(searchClientDto.getName(), searchClientDto.getSurname(), searchClientDto.getSecondName()).stream()
          .map(ClientDto::createInstance)
          .collect(Collectors.toList());
  }
  @Transactional
  public void removeClient(Long clientId){
    accountService.deleteAccountsByClientId(clientId);
    ClientEntity clientEntity = findClient(clientId);
    clientRepository.delete(clientEntity);
  }

  @Transactional
  public void setClientFlagDeleted(Long clientId){
    accountService.setAccountsFlagDeleted(clientId);
    ClientEntity clientEntity = findClient(clientId);
    clientEntity.setDeleted(true);
    clientRepository.save(clientEntity);
  }
}
