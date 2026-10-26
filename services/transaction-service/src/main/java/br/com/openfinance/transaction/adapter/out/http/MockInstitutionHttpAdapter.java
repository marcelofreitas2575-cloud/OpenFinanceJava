package br.com.openfinance.transaction.adapter.out.http;

import br.com.openfinance.transaction.domain.model.Transaction;
import br.com.openfinance.transaction.domain.port.InstitutionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Instant;
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
    public List<Transaction> fetchTransactions(
            String accountId, String consentId, Instant from, Instant to) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", apiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/internal/open-banking/v1/accounts/" + accountId + "/transactions")
                .queryParam("from", from.toString())
                .queryParam("to", to.toString())
                .toUriString();

        var response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, List<Map<String, Object>>>>() {
                });

        List<Map<String, Object>> data = response.getBody() != null ? response.getBody().get("data") : List.of();
        return data.stream().map(item -> mapTransaction(item, accountId, consentId)).toList();
    }

    private Transaction mapTransaction(Map<String, Object> item, String accountId, String consentId) {
        return new Transaction(
                String.valueOf(item.get("transactionId")),
                accountId,
                consentId,
                String.valueOf(item.get("type")),
                new BigDecimal(String.valueOf(item.get("amount"))),
                String.valueOf(item.get("currency")),
                Instant.parse(String.valueOf(item.get("transactionDate"))),
                String.valueOf(item.get("description"))
        );
    }
}
