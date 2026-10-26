package br.com.openfinance.mockinstitution.application;

import br.com.openfinance.mockinstitution.domain.model.BankTransaction;
import br.com.openfinance.mockinstitution.domain.port.TransactionQueryPort;

import java.time.Instant;
import java.util.List;

public class ListTransactionsUseCase {

    private final TransactionQueryPort transactionQueryPort;

    public ListTransactionsUseCase(TransactionQueryPort transactionQueryPort) {
        this.transactionQueryPort = transactionQueryPort;
    }

    public List<BankTransaction> execute(String accountId, Instant from, Instant to) {
        return transactionQueryPort.findByAccountId(accountId, from, to);
    }
}
