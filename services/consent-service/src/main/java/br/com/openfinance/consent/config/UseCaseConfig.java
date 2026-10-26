package br.com.openfinance.consent.config;

import br.com.openfinance.consent.application.CreateConsentUseCase;
import br.com.openfinance.consent.application.GetConsentUseCase;
import br.com.openfinance.consent.application.RevokeConsentUseCase;
import br.com.openfinance.consent.domain.port.ConsentEventPublisherPort;
import br.com.openfinance.consent.domain.port.ConsentRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    CreateConsentUseCase createConsentUseCase(
            ConsentRepositoryPort consentRepositoryPort,
            ConsentEventPublisherPort consentEventPublisherPort) {
        return new CreateConsentUseCase(consentRepositoryPort, consentEventPublisherPort);
    }

    @Bean
    GetConsentUseCase getConsentUseCase(ConsentRepositoryPort consentRepositoryPort) {
        return new GetConsentUseCase(consentRepositoryPort);
    }

    @Bean
    RevokeConsentUseCase revokeConsentUseCase(
            ConsentRepositoryPort consentRepositoryPort,
            ConsentEventPublisherPort consentEventPublisherPort) {
        return new RevokeConsentUseCase(consentRepositoryPort, consentEventPublisherPort);
    }
}
