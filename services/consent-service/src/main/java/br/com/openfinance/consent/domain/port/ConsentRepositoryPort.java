package br.com.openfinance.consent.domain.port;

import br.com.openfinance.consent.domain.model.Consent;

import java.util.Optional;

public interface ConsentRepositoryPort {

    Consent save(Consent consent);

    Optional<Consent> findById(String consentId);
}
