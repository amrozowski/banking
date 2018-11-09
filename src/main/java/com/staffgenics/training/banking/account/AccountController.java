package com.staffgenics.training.banking.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @RequestMapping(value = "/accounts", method = RequestMethod.GET)
  public List<AccountDto> getAccounts(){
    return accountService.getAccounts();
  }

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  public Long createAccount(@RequestBody AccountDto accountDto) {
    return accountService.createAccount(accountDto);
  }

  @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
  public AccountDto getAccount(@PathVariable Long id) {
    return accountService.getAccount(id);
  }

  @RequestMapping(value = "/account/{id}/operation", method = RequestMethod.POST)
  public Long referOperation(@RequestBody OperationDto operationDto, @PathVariable Long id){
    return accountService.referOperation(operationDto, id);
  }

  @RequestMapping(value = "/account/{id}/operations", method = RequestMethod.POST)
  public List<OperationDto> findOperations(@RequestBody SearchOperationDto searchOperationDto, @PathVariable Long id){
    return accountService.findOperations(searchOperationDto, id);
  }
}