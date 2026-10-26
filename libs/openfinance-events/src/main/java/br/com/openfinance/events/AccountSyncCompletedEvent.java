package br.com.openfinance.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountSyncCompletedEvent(
        String eventId,
        String consentId,
        String customerId,
        List<String> accountIds,
        Instant completedAt,
        String correlationId
) {
}
