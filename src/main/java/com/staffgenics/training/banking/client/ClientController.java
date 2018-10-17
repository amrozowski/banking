package com.staffgenics.training.banking.client;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler obsługujący klientów.
 */
@RestController
public class ClientController {

  private final ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @RequestMapping(value = "/clients", method = RequestMethod.GET)
  public List<ClientDto> getClients() {
    return clientService.getClients().stream()
        .map(ClientDto::createInstance)
        .collect(Collectors.toList());
  }
}
