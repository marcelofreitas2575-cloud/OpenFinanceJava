package br.com.openfinance.transaction.adapter.out.persistence;

import br.com.openfinance.transaction.domain.model.Transaction;
import br.com.openfinance.transaction.domain.port.TransactionRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final TransactionMongoRepository repository;

    public TransactionRepositoryAdapter(TransactionMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return;
        }
        Set<String> accountIds = new HashSet<>();
        String consentId = transactions.get(0).consentId();
        for (Transaction transaction : transactions) {
            accountIds.add(transaction.accountId());
        }
        for (String accountId : accountIds) {
            repository.deleteByConsentIdAndAccountId(consentId, accountId);
        }
        repository.saveAll(transactions.stream().map(this::toDocument).toList());
    }

    @Override
    public List<Transaction> findByAccountAndConsent(
            String accountId, String consentId, Instant from, Instant to) {
        return repository.findByAccountIdAndConsentIdAndTransactionDateBetween(accountId, consentId, from, to)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private TransactionDocument toDocument(Transaction transaction) {
        TransactionDocument document = new TransactionDocument();
        document.setId(UUID.randomUUID().toString());
        document.setTransactionId(transaction.transactionId());
        document.setAccountId(transaction.accountId());
        document.setConsentId(transaction.consentId());
        document.setType(transaction.type());
        document.setAmount(transaction.amount());
        document.setCurrency(transaction.currency());
        document.setTransactionDate(transaction.transactionDate());
        document.setDescription(transaction.description());
        return document;
    }

    private Transaction toDomain(TransactionDocument document) {
        return new Transaction(
                document.getTransactionId(),
                document.getAccountId(),
                document.getConsentId(),
                document.getType(),
                document.getAmount(),
                document.getCurrency(),
                document.getTransactionDate(),
                document.getDescription()
        );
    }
}
