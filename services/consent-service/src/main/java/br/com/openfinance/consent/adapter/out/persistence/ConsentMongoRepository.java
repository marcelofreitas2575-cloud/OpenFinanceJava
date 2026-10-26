package br.com.openfinance.consent.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsentMongoRepository extends MongoRepository<ConsentDocument, String> {
}
