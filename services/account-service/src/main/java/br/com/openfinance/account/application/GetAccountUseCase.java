package br.com.openfinance.account.application;

import br.com.openfinance.account.domain.model.Account;
import br.com.openfinance.account.domain.port.AccountRepositoryPort;
import br.com.openfinance.account.domain.port.ConsentValidationPort;

public class GetAccountUseCase {

    private final ConsentValidationPort consentValidationPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public GetAccountUseCase(
            ConsentValidationPort consentValidationPort,
            AccountRepositoryPort accountRepositoryPort) {
        this.consentValidationPort = consentValidationPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    public Account execute(String consentId, String customerId, String accountId) {
        consentValidationPort.validateAuthorised(consentId, customerId);
        return accountRepositoryPort.findByAccountIdAndConsentId(accountId, consentId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}
