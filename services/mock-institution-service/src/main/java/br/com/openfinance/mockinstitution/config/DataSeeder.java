package br.com.openfinance.mockinstitution.config;

import br.com.openfinance.mockinstitution.adapter.out.persistence.AccountDocument;
import br.com.openfinance.mockinstitution.adapter.out.persistence.AccountMongoRepository;
import br.com.openfinance.mockinstitution.adapter.out.persistence.TransactionDocument;
import br.com.openfinance.mockinstitution.adapter.out.persistence.TransactionMongoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedMockBank(
            AccountMongoRepository accountRepository,
            TransactionMongoRepository transactionRepository) {
        return args -> {
            if (accountRepository.count() > 0) {
                return;
            }
            AccountDocument checking = new AccountDocument();
            checking.setAccountId("acc-checking-001");
            checking.setCustomerId("customer-demo-001");
            checking.setBranchCode("0001");
            checking.setNumber("123456");
            checking.setCheckDigit("7");
            checking.setType("CACC");
            checking.setCurrency("BRL");
            checking.setAvailableAmount(new BigDecimal("5420.35"));
            checking.setBlockedAmount(BigDecimal.ZERO);

            AccountDocument savings = new AccountDocument();
            savings.setAccountId("acc-savings-001");
            savings.setCustomerId("customer-demo-001");
            savings.setBranchCode("0001");
            savings.setNumber("654321");
            savings.setCheckDigit("2");
            savings.setType("SVGS");
            savings.setCurrency("BRL");
            savings.setAvailableAmount(new BigDecimal("12000.00"));
            savings.setBlockedAmount(new BigDecimal("500.00"));

            accountRepository.saveAll(java.util.List.of(checking, savings));

            Instant now = Instant.now();
            TransactionDocument tx1 = new TransactionDocument();
            tx1.setTransactionId("tx-001");
            tx1.setAccountId("acc-checking-001");
            tx1.setType("CREDIT");
            tx1.setAmount(new BigDecimal("1500.00"));
            tx1.setCurrency("BRL");
            tx1.setTransactionDate(now.minus(2, ChronoUnit.DAYS));
            tx1.setDescription("Salario mock");

            TransactionDocument tx2 = new TransactionDocument();
            tx2.setTransactionId("tx-002");
            tx2.setAccountId("acc-checking-001");
            tx2.setType("DEBIT");
            tx2.setAmount(new BigDecimal("89.90"));
            tx2.setCurrency("BRL");
            tx2.setTransactionDate(now.minus(1, ChronoUnit.DAYS));
            tx2.setDescription("Supermercado mock");

            transactionRepository.saveAll(java.util.List.of(tx1, tx2));
        };
    }
}
