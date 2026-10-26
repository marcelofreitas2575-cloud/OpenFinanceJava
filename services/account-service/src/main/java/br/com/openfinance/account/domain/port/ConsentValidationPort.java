package br.com.openfinance.account.domain.port;

public interface ConsentValidationPort {

    void validateAuthorised(String consentId, String customerId);
}
