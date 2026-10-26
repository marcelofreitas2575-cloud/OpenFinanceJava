package br.com.openfinance.account.adapter.out.kafka;

import br.com.openfinance.account.domain.port.AccountEventPublisherPort;
import br.com.openfinance.events.AccountSyncCompletedEvent;
import br.com.openfinance.events.AccountSyncFailedEvent;
import br.com.openfinance.events.KafkaTopics;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class AccountKafkaPublisherAdapter implements AccountEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccountKafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishSyncCompleted(
            String consentId, String customerId, List<String> accountIds, String correlationId) {
        AccountSyncCompletedEvent event = new AccountSyncCompletedEvent(
                UUID.randomUUID().toString(),
                consentId,
                customerId,
                accountIds,
                Instant.now(),
                correlationId
        );
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(KafkaTopics.ACCOUNT_SYNC_COMPLETED, consentId, event);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(record);
    }

    @Override
    public void publishSyncFailed(String consentId, String reason, String correlationId) {
        AccountSyncFailedEvent event = new AccountSyncFailedEvent(
                UUID.randomUUID().toString(),
                consentId,
                reason,
                Instant.now(),
                correlationId
        );
        ProducerRecord<String, Object> record =
                new ProducerRecord<>(KafkaTopics.ACCOUNT_SYNC_FAILED, consentId, event);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(record);
    }
}
