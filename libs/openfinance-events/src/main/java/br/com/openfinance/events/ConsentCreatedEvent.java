package br.com.openfinance.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConsentCreatedEvent(
        String eventId,
        String consentId,
        String customerId,
        List<String> permissions,
        Instant authorisedAt,
        String correlationId
) {
}
