package br.com.openfinance.mockinstitution.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record BankTransaction(
        String transactionId,
        String accountId,
        String type,
        BigDecimal amount,
        String currency,
        Instant transactionDate,
        String description
) {
}
