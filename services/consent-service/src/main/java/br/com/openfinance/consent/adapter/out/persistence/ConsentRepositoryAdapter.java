package br.com.openfinance.consent.adapter.out.persistence;

import br.com.openfinance.consent.domain.model.Consent;
import br.com.openfinance.consent.domain.model.ConsentStatus;
import br.com.openfinance.consent.domain.port.ConsentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConsentRepositoryAdapter implements ConsentRepositoryPort {

    private final ConsentMongoRepository repository;

    public ConsentRepositoryAdapter(ConsentMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Consent save(Consent consent) {
        return toDomain(repository.save(toDocument(consent)));
    }

    @Override
    public Optional<Consent> findById(String consentId) {
        return repository.findById(consentId).map(this::toDomain);
    }

    private ConsentDocument toDocument(Consent consent) {
        ConsentDocument document = new ConsentDocument();
        document.setConsentId(consent.getConsentId());
        document.setCustomerId(consent.getCustomerId());
        document.setPermissions(consent.getPermissions());
        document.setStatus(consent.getStatus());
        document.setCreatedAt(consent.getCreatedAt());
        document.setAuthorisedAt(consent.getAuthorisedAt());
        document.setRevokedAt(consent.getRevokedAt());
        return document;
    }

    private Consent toDomain(ConsentDocument document) {
        return Consent.rehydrate(
                document.getConsentId(),
                document.getCustomerId(),
                document.getPermissions(),
                document.getStatus() != null ? document.getStatus() : ConsentStatus.AWAITING_AUTHORISATION,
                document.getCreatedAt(),
                document.getAuthorisedAt(),
                document.getRevokedAt()
        );
    }
}
