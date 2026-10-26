package br.com.openfinance.account.domain.port;

import br.com.openfinance.account.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepositoryPort {

    void saveAll(List<Account> accounts);

    List<Account> findByConsentId(String consentId);

    Optional<Account> findByAccountIdAndConsentId(String accountId, String consentId);
}
