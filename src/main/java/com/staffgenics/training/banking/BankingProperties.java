package com.staffgenics.training.banking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Klasa z konfiguracją aplikacji.
 */
@ConfigurationProperties(prefix = "banking")
@Getter
@Setter
public class BankingProperties {

  private int maxCardAmountOfSameType = 3;

  private int cardValidMonths;

  private String cronPeriodity;
}
