package br.com.openfinance.account.adapter.in.kafka;

import br.com.openfinance.account.adapter.out.redis.ConsentCacheRedisAdapter;
import br.com.openfinance.account.application.SyncAccountsUseCase;
import br.com.openfinance.events.ConsentCreatedEvent;
import br.com.openfinance.events.ConsentRevokedEvent;
import br.com.openfinance.events.KafkaTopics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConsentCreatedKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ConsentCreatedKafkaConsumer.class);

    private final SyncAccountsUseCase syncAccountsUseCase;
    private final ConsentCacheRedisAdapter consentCacheRedisAdapter;

    public ConsentCreatedKafkaConsumer(
            SyncAccountsUseCase syncAccountsUseCase,
            ConsentCacheRedisAdapter consentCacheRedisAdapter) {
        this.syncAccountsUseCase = syncAccountsUseCase;
        this.consentCacheRedisAdapter = consentCacheRedisAdapter;
    }

    @KafkaListener(topics = KafkaTopics.CONSENT_CREATED, groupId = "account-service")
    public void onConsentCreated(ConsentCreatedEvent event) {
        log.info("Syncing accounts for consent {}", event.consentId());
        consentCacheRedisAdapter.cacheAuthorised(event.consentId(), event.customerId());
        syncAccountsUseCase.execute(event.consentId(), event.customerId(), event.correlationId());
    }

    @KafkaListener(topics = KafkaTopics.CONSENT_REVOKED, groupId = "account-service")
    public void onConsentRevoked(ConsentRevokedEvent event) {
        consentCacheRedisAdapter.evict(event.consentId());
    }
}
