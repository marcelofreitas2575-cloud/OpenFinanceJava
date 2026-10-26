package br.com.openfinance.transaction.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {

    List<TransactionDocument> findByAccountIdAndConsentIdAndTransactionDateBetween(
            String accountId, String consentId, Instant from, Instant to);

    void deleteByConsentIdAndAccountId(String consentId, String accountId);
}
