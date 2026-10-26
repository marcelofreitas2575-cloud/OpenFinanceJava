package br.com.openfinance.account.domain.model;

import java.math.BigDecimal;

public record Account(
        String accountId,
        String consentId,
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
