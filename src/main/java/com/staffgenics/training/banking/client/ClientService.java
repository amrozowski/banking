package com.staffgenics.training.banking.client;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący klientów.
 */
@Service
class ClientService {

  private final ClientRepository clientRepository;

  @Autowired
  ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  List<ClientEntity> getClients() {
    return clientRepository.findAll();
  }
}
