package br.com.openfinance.account.adapter.out.http;

import br.com.openfinance.account.domain.model.Account;
import br.com.openfinance.account.domain.port.InstitutionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class MockInstitutionHttpAdapter implements InstitutionPort {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public MockInstitutionHttpAdapter(
            RestTemplate restTemplate,
            @Value("${integration.mock-institution.base-url}") String baseUrl,
            @Value("${integration.mock-institution.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public List<Account> fetchAccounts(String customerId, String consentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                baseUrl + "/internal/open-banking/v1/accounts?customerId=" + customerId,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, List<Map<String, Object>>>>() {
                });

        List<Map<String, Object>> data = response.getBody() != null ? response.getBody().get("data") : List.of();
        return data.stream().map(item -> mapAccount(item, consentId, customerId)).toList();
    }

    private Account mapAccount(Map<String, Object> item, String consentId, String customerId) {
        return new Account(
                String.valueOf(item.get("accountId")),
                consentId,
                customerId,
                String.valueOf(item.get("branchCode")),
                String.valueOf(item.get("number")),
                String.valueOf(item.get("checkDigit")),
                String.valueOf(item.get("type")),
                String.valueOf(item.get("currency")),
                new BigDecimal(String.valueOf(item.get("availableAmount"))),
                new BigDecimal(String.valueOf(item.get("blockedAmount")))
        );
    }
}
