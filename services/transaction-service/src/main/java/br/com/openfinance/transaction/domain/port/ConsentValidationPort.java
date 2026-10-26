package br.com.openfinance.transaction.domain.port;

public interface ConsentValidationPort {

    void validateAuthorised(String consentId, String customerId);
}
