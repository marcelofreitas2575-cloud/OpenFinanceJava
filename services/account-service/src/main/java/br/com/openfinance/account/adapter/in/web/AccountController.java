package br.com.openfinance.account.adapter.in.web;

import br.com.openfinance.account.application.GetAccountUseCase;
import br.com.openfinance.account.application.ListAccountsUseCase;
import br.com.openfinance.account.domain.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open-banking/v1/accounts")
public class AccountController {

    private final ListAccountsUseCase listAccountsUseCase;
    private final GetAccountUseCase getAccountUseCase;

    public AccountController(ListAccountsUseCase listAccountsUseCase, GetAccountUseCase getAccountUseCase) {
        this.listAccountsUseCase = listAccountsUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestHeader("X-Consent-Id") String consentId,
            @AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getClaimAsString("customerId");
        List<Account> accounts = listAccountsUseCase.execute(consentId, customerId);
        return ResponseEntity.ok(Map.of("data", accounts));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Map<String, Object>> get(
            @PathVariable String accountId,
            @RequestHeader("X-Consent-Id") String consentId,
            @AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getClaimAsString("customerId");
        Account account = getAccountUseCase.execute(consentId, customerId, accountId);
        return ResponseEntity.ok(Map.of("data", account));
    }

    @GetMapping("/{accountId}/balances")
    public ResponseEntity<Map<String, Object>> balances(
            @PathVariable String accountId,
            @RequestHeader("X-Consent-Id") String consentId,
            @AuthenticationPrincipal Jwt jwt) {
        String customerId = jwt.getClaimAsString("customerId");
        Account account = getAccountUseCase.execute(consentId, customerId, accountId);
        return ResponseEntity.ok(Map.of(
                "data",
                Map.of(
                        "availableAmount", account.availableAmount(),
                        "blockedAmount", account.blockedAmount(),
                        "currency", account.currency()
                )
        ));
    }
}
