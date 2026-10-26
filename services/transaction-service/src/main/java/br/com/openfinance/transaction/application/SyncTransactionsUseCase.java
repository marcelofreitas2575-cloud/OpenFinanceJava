package br.com.openfinance.transaction.application;

import br.com.openfinance.transaction.domain.model.Transaction;
import br.com.openfinance.transaction.domain.port.InstitutionPort;
import br.com.openfinance.transaction.domain.port.TransactionRepositoryPort;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SyncTransactionsUseCase {

    private final InstitutionPort institutionPort;
    private final TransactionRepositoryPort transactionRepositoryPort;

    public SyncTransactionsUseCase(
            InstitutionPort institutionPort,
            TransactionRepositoryPort transactionRepositoryPort) {
        this.institutionPort = institutionPort;
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    public void execute(String consentId, List<String> accountIds) {
        Instant from = Instant.parse("2020-01-01T00:00:00Z");
        Instant to = Instant.now();
        List<Transaction> all = new ArrayList<>();
        for (String accountId : accountIds) {
            all.addAll(institutionPort.fetchTransactions(accountId, consentId, from, to));
        }
        transactionRepositoryPort.saveAll(all);
    }
}
