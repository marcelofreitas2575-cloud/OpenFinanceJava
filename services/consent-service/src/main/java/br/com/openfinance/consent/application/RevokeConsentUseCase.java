package br.com.openfinance.consent.application;

import br.com.openfinance.consent.domain.model.Consent;
import br.com.openfinance.consent.domain.port.ConsentEventPublisherPort;
import br.com.openfinance.consent.domain.port.ConsentRepositoryPort;

import java.time.Instant;

public class RevokeConsentUseCase {

    private final ConsentRepositoryPort consentRepositoryPort;
    private final ConsentEventPublisherPort consentEventPublisherPort;

    public RevokeConsentUseCase(
            ConsentRepositoryPort consentRepositoryPort,
            ConsentEventPublisherPort consentEventPublisherPort) {
        this.consentRepositoryPort = consentRepositoryPort;
        this.consentEventPublisherPort = consentEventPublisherPort;
    }

    public Consent execute(String consentId, String correlationId) {
        Consent consent = consentRepositoryPort.findById(consentId)
                .orElseThrow(() -> new IllegalArgumentException("Consent not found"));
        consent.revoke(Instant.now());
        Consent saved = consentRepositoryPort.save(consent);
        consentEventPublisherPort.publishRevoked(saved, correlationId);
        return saved;
    }
}
