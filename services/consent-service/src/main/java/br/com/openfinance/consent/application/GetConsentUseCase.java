package br.com.openfinance.consent.application;

import br.com.openfinance.consent.domain.model.Consent;
import br.com.openfinance.consent.domain.port.ConsentRepositoryPort;

public class GetConsentUseCase {

    private final ConsentRepositoryPort consentRepositoryPort;

    public GetConsentUseCase(ConsentRepositoryPort consentRepositoryPort) {
        this.consentRepositoryPort = consentRepositoryPort;
    }

    public Consent execute(String consentId) {
        return consentRepositoryPort.findById(consentId)
                .orElseThrow(() -> new IllegalArgumentException("Consent not found"));
    }
}
