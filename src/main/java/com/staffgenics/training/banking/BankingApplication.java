package com.staffgenics.training.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Klasa z podstawową konfiguracją aplikacji.
 */
@SpringBootApplication
@EnableConfigurationProperties(BankingProperties.class)
@EnableScheduling
@EnableCaching
public class BankingApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingApplication.class, args);
  }
}
