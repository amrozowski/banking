package com.staffgenics.training.banking.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OperationService {

  private final OperationRepository operationRepository;

  @Autowired
  OperationService (OperationRepository operationRepository){
    this.operationRepository = operationRepository;
  }

  @Transactional
  Long addOperation(OperationEntity operationEntity){
    operationRepository.save(operationEntity);
    return operationEntity.getId();
  }
}
