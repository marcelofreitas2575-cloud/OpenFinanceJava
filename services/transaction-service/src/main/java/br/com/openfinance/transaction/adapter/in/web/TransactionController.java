package br.com.openfinance.transaction.adapter.in.web;

import br.com.openfinance.transaction.application.ListTransactionsUseCase;
import br.com.openfinance.transaction.domain.model.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open-banking/v1/accounts/{accountId}/transactions")
public class TransactionController {

    private final ListTransactionsUseCase listTransactionsUseCase;

    public TransactionController(ListTransactionsUseCase listTransactionsUseCase) {
        this.listTransactionsUseCase = listTransactionsUseCase;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @PathVariable String accountId,
            @RequestHeader("X-Consent-Id") String consentId,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        String customerId = jwt.getClaimAsString("customerId");
        Instant effectiveFrom = from != null ? from : Instant.parse("2020-01-01T00:00:00Z");
        Instant effectiveTo = to != null ? to : Instant.now();
        List<Transaction> transactions = listTransactionsUseCase.execute(
                consentId, customerId, accountId, effectiveFrom, effectiveTo);
        return ResponseEntity.ok(Map.of("data", transactions));
    }
}
