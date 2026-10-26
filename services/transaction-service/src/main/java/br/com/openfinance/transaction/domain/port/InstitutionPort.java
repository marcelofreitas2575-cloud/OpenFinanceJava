package br.com.openfinance.transaction.domain.port;

import br.com.openfinance.transaction.domain.model.Transaction;

import java.time.Instant;
import java.util.List;

public interface InstitutionPort {

    List<Transaction> fetchTransactions(String accountId, String consentId, Instant from, Instant to);
}
