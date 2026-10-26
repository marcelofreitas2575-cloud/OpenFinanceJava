package br.com.openfinance.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsentRevokedEvent(
        String eventId,
        String consentId,
        Instant revokedAt,
        String correlationId
) {
}
