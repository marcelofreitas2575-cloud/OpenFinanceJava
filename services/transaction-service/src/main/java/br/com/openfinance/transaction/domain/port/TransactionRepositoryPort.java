package br.com.openfinance.transaction.domain.port;

import br.com.openfinance.transaction.domain.model.Transaction;

import java.time.Instant;
import java.util.List;

public interface TransactionRepositoryPort {

    void saveAll(List<Transaction> transactions);

    List<Transaction> findByAccountAndConsent(String accountId, String consentId, Instant from, Instant to);
}
