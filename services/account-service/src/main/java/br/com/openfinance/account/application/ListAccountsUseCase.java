package br.com.openfinance.account.application;

import br.com.openfinance.account.domain.model.Account;
import br.com.openfinance.account.domain.port.AccountRepositoryPort;
import br.com.openfinance.account.domain.port.ConsentValidationPort;

import java.util.List;

public class ListAccountsUseCase {

    private final ConsentValidationPort consentValidationPort;
    private final AccountRepositoryPort accountRepositoryPort;

    public ListAccountsUseCase(
            ConsentValidationPort consentValidationPort,
            AccountRepositoryPort accountRepositoryPort) {
        this.consentValidationPort = consentValidationPort;
        this.accountRepositoryPort = accountRepositoryPort;
    }

    public List<Account> execute(String consentId, String customerId) {
        consentValidationPort.validateAuthorised(consentId, customerId);
        List<Account> accounts = accountRepositoryPort.findByConsentId(consentId);
        if (accounts.isEmpty()) {
            throw new IllegalStateException("Accounts not synced yet for consent");
        }
        return accounts;
    }
}
