package br.com.openfinance.account.adapter.out.persistence;

import br.com.openfinance.account.domain.model.Account;
import br.com.openfinance.account.domain.port.AccountRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountMongoRepository repository;

    public AccountRepositoryAdapter(AccountMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Account> accounts) {
        if (accounts.isEmpty()) {
            return;
        }
        repository.deleteByConsentId(accounts.get(0).consentId());
        repository.saveAll(accounts.stream().map(this::toDocument).toList());
    }

    @Override
    public List<Account> findByConsentId(String consentId) {
        return repository.findByConsentId(consentId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Account> findByAccountIdAndConsentId(String accountId, String consentId) {
        return repository.findByAccountIdAndConsentId(accountId, consentId).map(this::toDomain);
    }

    private AccountDocument toDocument(Account account) {
        AccountDocument document = new AccountDocument();
        document.setId(UUID.randomUUID().toString());
        document.setAccountId(account.accountId());
        document.setConsentId(account.consentId());
        document.setCustomerId(account.customerId());
        document.setBranchCode(account.branchCode());
        document.setNumber(account.number());
        document.setCheckDigit(account.checkDigit());
        document.setType(account.type());
        document.setCurrency(account.currency());
        document.setAvailableAmount(account.availableAmount());
        document.setBlockedAmount(account.blockedAmount());
        return document;
    }

    private Account toDomain(AccountDocument document) {
        return new Account(
                document.getAccountId(),
                document.getConsentId(),
                document.getCustomerId(),
                document.getBranchCode(),
                document.getNumber(),
                document.getCheckDigit(),
                document.getType(),
                document.getCurrency(),
                document.getAvailableAmount(),
                document.getBlockedAmount()
        );
    }
}
