package br.com.openfinance.mockinstitution.domain.port;

import br.com.openfinance.mockinstitution.domain.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface AccountQueryPort {

    List<BankAccount> findByCustomerId(String customerId);

    Optional<BankAccount> findByAccountId(String accountId);
}
