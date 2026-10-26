package br.com.openfinance.transaction.adapter.in.kafka;

import br.com.openfinance.events.AccountSyncCompletedEvent;
import br.com.openfinance.events.ConsentRevokedEvent;
import br.com.openfinance.events.KafkaTopics;
import br.com.openfinance.transaction.application.SyncTransactionsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountSyncCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(AccountSyncCompletedConsumer.class);

    private final SyncTransactionsUseCase syncTransactionsUseCase;

    public AccountSyncCompletedConsumer(SyncTransactionsUseCase syncTransactionsUseCase) {
        this.syncTransactionsUseCase = syncTransactionsUseCase;
    }

    @KafkaListener(topics = KafkaTopics.ACCOUNT_SYNC_COMPLETED, groupId = "transaction-service")
    public void onAccountSyncCompleted(AccountSyncCompletedEvent event) {
        log.info("Syncing transactions for consent {}", event.consentId());
        syncTransactionsUseCase.execute(event.consentId(), event.accountIds());
    }

    @KafkaListener(topics = KafkaTopics.CONSENT_REVOKED, groupId = "transaction-service")
    public void onConsentRevoked(ConsentRevokedEvent event) {
        log.info("Consent revoked {}, skipping future sync", event.consentId());
    }
}
