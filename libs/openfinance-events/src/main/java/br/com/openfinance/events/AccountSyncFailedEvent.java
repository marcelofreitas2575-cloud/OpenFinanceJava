package br.com.openfinance.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountSyncFailedEvent(
        String eventId,
        String consentId,
        String reason,
        Instant failedAt,
        String correlationId
) {
}
