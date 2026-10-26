package br.com.openfinance.account.domain.port;

import java.util.List;

public interface AccountEventPublisherPort {

    void publishSyncCompleted(String consentId, String customerId, List<String> accountIds, String correlationId);

    void publishSyncFailed(String consentId, String reason, String correlationId);
}
