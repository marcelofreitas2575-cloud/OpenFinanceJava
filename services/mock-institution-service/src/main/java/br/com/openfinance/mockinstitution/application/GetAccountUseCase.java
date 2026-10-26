package br.com.openfinance.mockinstitution.application;

import br.com.openfinance.mockinstitution.domain.model.BankAccount;
import br.com.openfinance.mockinstitution.domain.port.AccountQueryPort;

import java.util.Optional;

public class GetAccountUseCase {

    private final AccountQueryPort accountQueryPort;

    public GetAccountUseCase(AccountQueryPort accountQueryPort) {
        this.accountQueryPort = accountQueryPort;
    }

    public Optional<BankAccount> execute(String accountId) {
        return accountQueryPort.findByAccountId(accountId);
    }
}
