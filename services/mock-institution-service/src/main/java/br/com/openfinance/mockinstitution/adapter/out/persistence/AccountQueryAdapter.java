package br.com.openfinance.mockinstitution.adapter.out.persistence;

import br.com.openfinance.mockinstitution.domain.model.BankAccount;
import br.com.openfinance.mockinstitution.domain.port.AccountQueryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AccountQueryAdapter implements AccountQueryPort {

    private final AccountMongoRepository repository;

    public AccountQueryAdapter(AccountMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BankAccount> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<BankAccount> findByAccountId(String accountId) {
        return repository.findById(accountId).map(this::toDomain);
    }

    private BankAccount toDomain(AccountDocument document) {
        return new BankAccount(
                document.getAccountId(),
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
