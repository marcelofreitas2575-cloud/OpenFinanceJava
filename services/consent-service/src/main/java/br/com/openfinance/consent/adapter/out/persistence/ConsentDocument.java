package br.com.openfinance.consent.adapter.out.persistence;

import br.com.openfinance.consent.domain.model.ConsentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "consents")
public class ConsentDocument {

    @Id
    private String consentId;
    private String customerId;
    private List<String> permissions;
    private ConsentStatus status;
    private Instant createdAt;
    private Instant authorisedAt;
    private Instant revokedAt;

    public String getConsentId() {
        return consentId;
    }

    public void setConsentId(String consentId) {
        this.consentId = consentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getAuthorisedAt() {
        return authorisedAt;
    }

    public void setAuthorisedAt(Instant authorisedAt) {
        this.authorisedAt = authorisedAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }
}
