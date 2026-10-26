package br.com.openfinance.mockinstitution.domain.model;

import java.math.BigDecimal;

public record BankAccount(
        String accountId,
        String customerId,
        String branchCode,
        String number,
        String checkDigit,
        String type,
        String currency,
        BigDecimal availableAmount,
        BigDecimal blockedAmount
) {
}
