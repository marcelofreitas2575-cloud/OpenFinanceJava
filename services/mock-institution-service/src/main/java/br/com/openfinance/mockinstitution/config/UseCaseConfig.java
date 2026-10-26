package br.com.openfinance.mockinstitution.config;

import br.com.openfinance.mockinstitution.application.GetAccountUseCase;
import br.com.openfinance.mockinstitution.application.ListAccountsUseCase;
import br.com.openfinance.mockinstitution.application.ListTransactionsUseCase;
import br.com.openfinance.mockinstitution.domain.port.AccountQueryPort;
import br.com.openfinance.mockinstitution.domain.port.TransactionQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    ListAccountsUseCase listAccountsUseCase(AccountQueryPort accountQueryPort) {
        return new ListAccountsUseCase(accountQueryPort);
    }

    @Bean
    GetAccountUseCase getAccountUseCase(AccountQueryPort accountQueryPort) {
        return new GetAccountUseCase(accountQueryPort);
    }

    @Bean
    ListTransactionsUseCase listTransactionsUseCase(TransactionQueryPort transactionQueryPort) {
        return new ListTransactionsUseCase(transactionQueryPort);
    }
}
