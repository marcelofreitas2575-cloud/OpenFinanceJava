package br.com.openfinance.mockinstitution.application;

import br.com.openfinance.mockinstitution.domain.model.BankAccount;
import br.com.openfinance.mockinstitution.domain.port.AccountQueryPort;

import java.util.List;

public class ListAccountsUseCase {

    private final AccountQueryPort accountQueryPort;

    public ListAccountsUseCase(AccountQueryPort accountQueryPort) {
        this.accountQueryPort = accountQueryPort;
    }

    public List<BankAccount> execute(String customerId) {
        return accountQueryPort.findByCustomerId(customerId);
    }
}
