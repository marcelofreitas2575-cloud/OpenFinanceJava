package br.com.openfinance.mockinstitution.adapter.out.persistence;

import br.com.openfinance.mockinstitution.domain.model.BankTransaction;
import br.com.openfinance.mockinstitution.domain.port.TransactionQueryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TransactionQueryAdapter implements TransactionQueryPort {

    private final TransactionMongoRepository repository;

    public TransactionQueryAdapter(TransactionMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BankTransaction> findByAccountId(String accountId, Instant from, Instant to) {
        return repository.findByAccountIdAndTransactionDateBetween(accountId, from, to).stream()
                .map(this::toDomain)
                .toList();
    }

    private BankTransaction toDomain(TransactionDocument document) {
        return new BankTransaction(
                document.getTransactionId(),
                document.getAccountId(),
                document.getType(),
                document.getAmount(),
                document.getCurrency(),
                document.getTransactionDate(),
                document.getDescription()
        );
    }
}
