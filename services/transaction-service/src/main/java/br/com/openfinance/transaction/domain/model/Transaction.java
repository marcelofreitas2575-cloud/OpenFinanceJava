package br.com.openfinance.transaction.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        String transactionId,
        String accountId,
        String consentId,
        String type,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        String description
) {
}
