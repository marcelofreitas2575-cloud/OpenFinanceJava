package br.com.openfinance.mockinstitution.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {

    List<AccountDocument> findByCustomerId(String customerId);
}
