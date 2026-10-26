package br.com.openfinance.consent.adapter.in.web;

import br.com.openfinance.consent.adapter.in.web.dto.CreateConsentRequest;
import br.com.openfinance.consent.application.CreateConsentUseCase;
import br.com.openfinance.consent.application.GetConsentUseCase;
import br.com.openfinance.consent.application.RevokeConsentUseCase;
import br.com.openfinance.consent.domain.model.Consent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/open-banking/v1/consents")
public class ConsentController {

    private final CreateConsentUseCase createConsentUseCase;
    private final GetConsentUseCase getConsentUseCase;
    private final RevokeConsentUseCase revokeConsentUseCase;

    public ConsentController(
            CreateConsentUseCase createConsentUseCase,
            GetConsentUseCase getConsentUseCase,
            RevokeConsentUseCase revokeConsentUseCase) {
        this.createConsentUseCase = createConsentUseCase;
        this.getConsentUseCase = getConsentUseCase;
        this.revokeConsentUseCase = revokeConsentUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody CreateConsentRequest request,
            @AuthenticationPrincipal Jwt jwt,
            HttpServletRequest servletRequest) {
        String customerId = jwt.getClaimAsString("customerId");
        Consent consent = createConsentUseCase.execute(
                customerId,
                request.permissions(),
                correlationId(servletRequest)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("data", consent));
    }

    @GetMapping("/{consentId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String consentId) {
        return ResponseEntity.ok(Map.of("data", getConsentUseCase.execute(consentId)));
    }

    @PatchMapping("/{consentId}")
    public ResponseEntity<Map<String, Object>> revoke(
            @PathVariable String consentId,
            HttpServletRequest servletRequest) {
        Consent consent = revokeConsentUseCase.execute(consentId, correlationId(servletRequest));
        return ResponseEntity.ok(Map.of("data", consent));
    }

    @DeleteMapping("/{consentId}")
    public ResponseEntity<Void> delete(
            @PathVariable String consentId,
            HttpServletRequest servletRequest) {
        revokeConsentUseCase.execute(consentId, correlationId(servletRequest));
        return ResponseEntity.noContent().build();
    }

    private String correlationId(HttpServletRequest request) {
        String header = request.getHeader("X-Correlation-Id");
        return header != null && !header.isBlank() ? header : java.util.UUID.randomUUID().toString();
    }
}
