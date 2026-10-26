package br.com.openfinance.account.config;

import br.com.openfinance.account.application.GetAccountUseCase;
import br.com.openfinance.account.application.ListAccountsUseCase;
import br.com.openfinance.account.application.SyncAccountsUseCase;
import br.com.openfinance.account.domain.port.AccountEventPublisherPort;
import br.com.openfinance.account.domain.port.AccountRepositoryPort;
import br.com.openfinance.account.domain.port.ConsentValidationPort;
import br.com.openfinance.account.domain.port.InstitutionPort;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    SyncAccountsUseCase syncAccountsUseCase(
            InstitutionPort institutionPort,
            AccountRepositoryPort accountRepositoryPort,
            AccountEventPublisherPort accountEventPublisherPort) {
        return new SyncAccountsUseCase(institutionPort, accountRepositoryPort, accountEventPublisherPort);
    }

    @Bean
    ListAccountsUseCase listAccountsUseCase(
            ConsentValidationPort consentValidationPort,
            AccountRepositoryPort accountRepositoryPort) {
        return new ListAccountsUseCase(consentValidationPort, accountRepositoryPort);
    }

    @Bean
    GetAccountUseCase getAccountUseCase(
            ConsentValidationPort consentValidationPort,
            AccountRepositoryPort accountRepositoryPort) {
        return new GetAccountUseCase(consentValidationPort, accountRepositoryPort);
    }

    @Bean
    NewTopic accountSyncCompletedTopic() {
        return TopicBuilder.name("of.account.sync.completed.v1").partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic accountSyncFailedTopic() {
        return TopicBuilder.name("of.account.sync.failed.v1").partitions(1).replicas(1).build();
    }
}
