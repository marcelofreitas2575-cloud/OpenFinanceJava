package br.com.openfinance.mockinstitution.adapter.in.web;

import br.com.openfinance.mockinstitution.application.GetAccountUseCase;
import br.com.openfinance.mockinstitution.application.ListAccountsUseCase;
import br.com.openfinance.mockinstitution.application.ListTransactionsUseCase;
import br.com.openfinance.mockinstitution.domain.model.BankAccount;
import br.com.openfinance.mockinstitution.domain.model.BankTransaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/open-banking/v1")
public class OpenBankingController {

    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final ListTransactionsUseCase listTransactionsUseCase;

    public OpenBankingController(
            ListAccountsUseCase listAccountsUseCase,
            GetAccountUseCase getAccountUseCase,
            ListTransactionsUseCase listTransactionsUseCase) {
        this.listAccountsUseCase = listAccountsUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.listTransactionsUseCase = listTransactionsUseCase;
    }

    @GetMapping("/accounts")
    public ResponseEntity<Map<String, Object>> listAccounts(@RequestParam String customerId) {
        List<BankAccount> accounts = listAccountsUseCase.execute(customerId);
        return ResponseEntity.ok(Map.of("data", accounts));
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Map<String, Object>> getAccount(@PathVariable String accountId) {
        return getAccountUseCase.execute(accountId)
                .map(account -> ResponseEntity.<Map<String, Object>>ok(Map.of("data", (Object) account)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<Map<String, Object>> listTransactions(
            @PathVariable String accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        Instant effectiveFrom = from != null ? from : Instant.parse("2020-01-01T00:00:00Z");
        Instant effectiveTo = to != null ? to : Instant.now();
        List<BankTransaction> transactions = listTransactionsUseCase.execute(accountId, effectiveFrom, effectiveTo);
        return ResponseEntity.ok(Map.of("data", transactions));
    }
}
