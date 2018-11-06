package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;

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

  private boolean validatePesel(ClientDto clientDto){
    String pesel = clientDto.getPesel();
    //length
    if (pesel.length() != 11) {
      log.info("Budowa numeru PESEL nie jest poprawna.");
      return false;
    }
    //allDigits and checksum
    boolean allDigits = true;
    int checkSum = 0;
    int[] weights = {1,3,7,9,1,3,7,9,1,3};

    for (int i = 0; i < pesel.length(); i++) {
      //all digits
      if (Character.isDigit(pesel.charAt(i))){
        allDigits = true;
      } else {
        log.info("Budowa numeru PESEL nie jest poprawna.");
        return false;
      }
      //checksum
      if (i!=10) checkSum += (weights[i] * Character.getNumericValue(pesel.charAt(i))) % 10;
    }
    checkSum = 10 - (checkSum % 10);
    boolean isCheckSumValid = checkSum == Character.getNumericValue(pesel.charAt(pesel.length() - 1));
    if (!isCheckSumValid){
      log.info("Budowa numeru PESEL nie jest poprawna.");
      return false;
    }
    log.info("Budowa numeru PESEL jest poprawna.");
    return true;
  }

  private void verifyPersonalData(ClientDto clientDto) {
    Optional<ClientEntity> clientEntityOptional = clientRepository.findClientByPesel(clientDto.getPesel());
    if(clientEntityOptional.isPresent()) {
      ClientEntity clientEntity = clientEntityOptional.get();
      if (clientDto.getName().equals(clientEntity.getName()) && clientDto.getSecondName().equals(clientEntity.getSecondName()) &&
          clientDto.getSurname().equals(clientEntity.getSurname())) {
        throw new IllegalArgumentException("Klient o podanym numerze PESEL istnieje - dane osobowe zgodne");
      } else if (clientEntity.getPesel().equals(clientDto.getPesel())) {
        throw new IllegalArgumentException("Klient o podanym numerze PESEL istnieje - dane osobowe niezgodne");
      }
    }
  }
  Long createClient(ClientDto clientDto) {
    log.info("Dodajemy nowego klienta");
    if(!clientDto.isForeigner()){
      if(!validatePesel(clientDto)){
        throw new IllegalArgumentException("Nieprawidłowy numer PESEL");
      }
      verifyPersonalData(clientDto);
    } else {
      clientDto.setPesel("FOREIGNER");
    }
    ClientEntity clientEntity = ClientEntity.createInstance(clientDto);
    clientRepository.save(clientEntity);
    return clientEntity.getId();
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
    if(searchClientDto.getName() != null && searchClientDto.getSurname() != null && searchClientDto.getSecondName() != null){
      return clientRepository.findClientByCriteria(searchClientDto.getName(), searchClientDto.getSurname(), searchClientDto.getSecondName()).stream()
          .map(ClientDto::createInstance)
          .collect(Collectors.toList());
    } else if(searchClientDto.getName() != null && searchClientDto.getSurname() != null){
      return clientRepository.findClientByCriteria(searchClientDto.getName(), searchClientDto.getSurname()).stream()
          .map(ClientDto::createInstance)
          .collect(Collectors.toList());
    } else if(searchClientDto.getSurname() != null){
      return clientRepository.findClientByCriteria(searchClientDto.getSurname()).stream()
          .map(ClientDto::createInstance)
          .collect(Collectors.toList());
    }
    return null;
  }
}
