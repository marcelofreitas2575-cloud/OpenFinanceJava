package br.com.openfinance.consent.domain.port;

import br.com.openfinance.consent.domain.model.Consent;

public interface ConsentEventPublisherPort {

    void publishCreated(Consent consent, String correlationId);

    void publishRevoked(Consent consent, String correlationId);
}
