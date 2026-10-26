package br.com.openfinance.consent.application;

import br.com.openfinance.consent.domain.model.Consent;
import br.com.openfinance.consent.domain.model.ConsentStatus;
import br.com.openfinance.consent.domain.port.ConsentEventPublisherPort;
import br.com.openfinance.consent.domain.port.ConsentRepositoryPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CreateConsentUseCase {

    private final ConsentRepositoryPort consentRepositoryPort;
    private final ConsentEventPublisherPort consentEventPublisherPort;

    public CreateConsentUseCase(
            ConsentRepositoryPort consentRepositoryPort,
            ConsentEventPublisherPort consentEventPublisherPort) {
        this.consentRepositoryPort = consentRepositoryPort;
        this.consentEventPublisherPort = consentEventPublisherPort;
    }

    public Consent execute(String customerId, List<String> permissions, String correlationId) {
        Consent consent = new Consent(
                UUID.randomUUID().toString(),
                customerId,
                permissions,
                ConsentStatus.AWAITING_AUTHORISATION,
                Instant.now()
        );
        consent.authorise(Instant.now());
        Consent saved = consentRepositoryPort.save(consent);
        consentEventPublisherPort.publishCreated(saved, correlationId);
        return saved;
    }
}
