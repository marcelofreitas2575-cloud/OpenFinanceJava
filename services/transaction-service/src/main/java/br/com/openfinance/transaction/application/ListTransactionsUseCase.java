package br.com.openfinance.transaction.application;

import br.com.openfinance.transaction.domain.model.Transaction;
import br.com.openfinance.transaction.domain.port.ConsentValidationPort;
import br.com.openfinance.transaction.domain.port.TransactionRepositoryPort;

import java.time.Instant;
import java.util.List;

public class ListTransactionsUseCase {

    private final ConsentValidationPort consentValidationPort;
    private final TransactionRepositoryPort transactionRepositoryPort;

    public ListTransactionsUseCase(
            ConsentValidationPort consentValidationPort,
            TransactionRepositoryPort transactionRepositoryPort) {
        this.consentValidationPort = consentValidationPort;
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    public List<Transaction> execute(
            String consentId, String customerId, String accountId, Instant from, Instant to) {
        consentValidationPort.validateAuthorised(consentId, customerId);
        List<Transaction> transactions =
                transactionRepositoryPort.findByAccountAndConsent(accountId, consentId, from, to);
        if (transactions.isEmpty()) {
            throw new IllegalStateException("Transactions not synced yet for account");
        }
        return transactions;
    }
}
