package com.staffgenics.training.banking.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    return clientService.getClients();
  }

  @RequestMapping(value = "/client", method = RequestMethod.POST)
  public Long createClient(@RequestBody ClientDto clientDto) {
    return clientService.createClient(clientDto);
  }

  @RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
  public ClientDto getClient(@PathVariable Long id) {
    return clientService.getClient(id);
  }

  @RequestMapping(value = "/clients", method = RequestMethod.POST)
  public List<ClientDto> getClientByCriteria(@RequestBody SearchClientDto searchClientDto) {
    return clientService.getClientByCriteria(searchClientDto);
  }

  @RequestMapping(value = "/client/{id}", method = RequestMethod.PUT)
  public void createClient(@RequestBody ClientDto clientDto, @PathVariable Long id) {
    clientService.editClient(clientDto, id);
  }

  @RequestMapping(value = "/client/{id}", method = RequestMethod.DELETE)
  public void removeClient(@PathVariable Long id) {
    clientService.removeClient(id);
  }

  @RequestMapping(value = "/client/flag/{id}", method = RequestMethod.DELETE)
  public void setClientFlagDelete(@PathVariable Long id) {
    clientService.setClientFlagDeleted(id);
  }
}
