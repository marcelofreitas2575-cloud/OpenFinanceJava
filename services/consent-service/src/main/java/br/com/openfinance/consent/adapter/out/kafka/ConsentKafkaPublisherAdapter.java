package br.com.openfinance.consent.adapter.out.kafka;

import br.com.openfinance.consent.domain.model.Consent;
import br.com.openfinance.consent.domain.port.ConsentEventPublisherPort;
import br.com.openfinance.events.ConsentCreatedEvent;
import br.com.openfinance.events.ConsentRevokedEvent;
import br.com.openfinance.events.KafkaTopics;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class ConsentKafkaPublisherAdapter implements ConsentEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ConsentKafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishCreated(Consent consent, String correlationId) {
        ConsentCreatedEvent event = new ConsentCreatedEvent(
                UUID.randomUUID().toString(),
                consent.getConsentId(),
                consent.getCustomerId(),
                consent.getPermissions(),
                consent.getAuthorisedAt(),
                correlationId
        );
        ProducerRecord<String, Object> record = new ProducerRecord<>(KafkaTopics.CONSENT_CREATED, consent.getConsentId(), event);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(record);
    }

    @Override
    public void publishRevoked(Consent consent, String correlationId) {
        ConsentRevokedEvent event = new ConsentRevokedEvent(
                UUID.randomUUID().toString(),
                consent.getConsentId(),
                consent.getRevokedAt(),
                correlationId
        );
        ProducerRecord<String, Object> record = new ProducerRecord<>(KafkaTopics.CONSENT_REVOKED, consent.getConsentId(), event);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(record);
    }
}
