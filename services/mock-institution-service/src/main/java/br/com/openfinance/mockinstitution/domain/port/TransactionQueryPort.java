package br.com.openfinance.mockinstitution.domain.port;

import br.com.openfinance.mockinstitution.domain.model.BankTransaction;

import java.time.Instant;
import java.util.List;

public interface TransactionQueryPort {

    List<BankTransaction> findByAccountId(String accountId, Instant from, Instant to);
}
