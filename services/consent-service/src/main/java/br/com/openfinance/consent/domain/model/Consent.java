package br.com.openfinance.consent.domain.model;

import java.time.Instant;
import java.util.List;

public class Consent {

    private final String consentId;
    private final String customerId;
    private final List<String> permissions;
    private ConsentStatus status;
    private final Instant createdAt;
    private Instant authorisedAt;
    private Instant revokedAt;

    public Consent(
            String consentId,
            String customerId,
            List<String> permissions,
            ConsentStatus status,
            Instant createdAt) {
        this.consentId = consentId;
        this.customerId = customerId;
        this.permissions = permissions;
        this.status = status;
        this.createdAt = createdAt;
    }

    public void authorise(Instant when) {
        if (status == ConsentStatus.REVOKED) {
            throw new IllegalStateException("Consent already revoked");
        }
        this.status = ConsentStatus.AUTHORISED;
        this.authorisedAt = when;
    }

    public void revoke(Instant when) {
        if (status == ConsentStatus.REVOKED) {
            throw new IllegalStateException("Consent already revoked");
        }
        this.status = ConsentStatus.REVOKED;
        this.revokedAt = when;
    }

    public String getConsentId() {
        return consentId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getAuthorisedAt() {
        return authorisedAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public static Consent rehydrate(
            String consentId,
            String customerId,
            java.util.List<String> permissions,
            ConsentStatus status,
            Instant createdAt,
            Instant authorisedAt,
            Instant revokedAt) {
        Consent consent = new Consent(consentId, customerId, permissions, status, createdAt);
        consent.status = status;
        consent.authorisedAt = authorisedAt;
        consent.revokedAt = revokedAt;
        return consent;
    }
}
