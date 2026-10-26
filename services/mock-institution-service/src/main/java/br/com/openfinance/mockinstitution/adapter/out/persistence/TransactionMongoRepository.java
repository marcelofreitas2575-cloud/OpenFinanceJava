package br.com.openfinance.mockinstitution.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface TransactionMongoRepository extends MongoRepository<TransactionDocument, String> {

    List<TransactionDocument> findByAccountIdAndTransactionDateBetween(
            String accountId, Instant from, Instant to);
}
