package br.com.openfinance.account.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AccountMongoRepository extends MongoRepository<AccountDocument, String> {

    List<AccountDocument> findByConsentId(String consentId);

    Optional<AccountDocument> findByAccountIdAndConsentId(String accountId, String consentId);

    void deleteByConsentId(String consentId);
}
