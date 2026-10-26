package br.com.openfinance.account.application;

import br.com.openfinance.account.domain.model.Account;
import br.com.openfinance.account.domain.port.AccountEventPublisherPort;
import br.com.openfinance.account.domain.port.AccountRepositoryPort;
import br.com.openfinance.account.domain.port.InstitutionPort;

import java.util.List;

public class SyncAccountsUseCase {

    private final InstitutionPort institutionPort;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountEventPublisherPort accountEventPublisherPort;

    public SyncAccountsUseCase(
            InstitutionPort institutionPort,
            AccountRepositoryPort accountRepositoryPort,
            AccountEventPublisherPort accountEventPublisherPort) {
        this.institutionPort = institutionPort;
        this.accountRepositoryPort = accountRepositoryPort;
        this.accountEventPublisherPort = accountEventPublisherPort;
    }

    public void execute(String consentId, String customerId, String correlationId) {
        try {
            List<Account> accounts = institutionPort.fetchAccounts(customerId, consentId);
            accountRepositoryPort.saveAll(accounts);
            List<String> accountIds = accounts.stream().map(Account::accountId).toList();
            accountEventPublisherPort.publishSyncCompleted(consentId, customerId, accountIds, correlationId);
        } catch (RuntimeException ex) {
            accountEventPublisherPort.publishSyncFailed(consentId, ex.getMessage(), correlationId);
            throw ex;
        }
    }
}
