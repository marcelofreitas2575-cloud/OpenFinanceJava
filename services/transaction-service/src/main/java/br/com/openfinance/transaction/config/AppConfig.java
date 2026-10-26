package br.com.openfinance.transaction.config;

import br.com.openfinance.transaction.application.ListTransactionsUseCase;
import br.com.openfinance.transaction.application.SyncTransactionsUseCase;
import br.com.openfinance.transaction.domain.port.ConsentValidationPort;
import br.com.openfinance.transaction.domain.port.InstitutionPort;
import br.com.openfinance.transaction.domain.port.TransactionRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SyncTransactionsUseCase syncTransactionsUseCase(
            InstitutionPort institutionPort,
            TransactionRepositoryPort transactionRepositoryPort) {
        return new SyncTransactionsUseCase(institutionPort, transactionRepositoryPort);
    }

    @Bean
    ListTransactionsUseCase listTransactionsUseCase(
            ConsentValidationPort consentValidationPort,
            TransactionRepositoryPort transactionRepositoryPort) {
        return new ListTransactionsUseCase(consentValidationPort, transactionRepositoryPort);
    }
}
