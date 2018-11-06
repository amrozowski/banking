package com.staffgenics.training.banking.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SearchClientDto {
  private String name;
  private String secondName;
  private String surname;
  private boolean vip;
}
