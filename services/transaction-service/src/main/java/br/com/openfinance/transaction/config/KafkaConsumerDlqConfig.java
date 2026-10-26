package br.com.openfinance.transaction.config;

import br.com.openfinance.events.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerDlqConfig {

    private static final long RETRY_INTERVAL_MS = 2_000L;
    private static final long MAX_RETRIES = 3L;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(KafkaTopics.dlqFor(record.topic()), record.partition()));

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(RETRY_INTERVAL_MS, MAX_RETRIES));
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    NewTopic accountSyncCompletedDlqTopic() {
        return TopicBuilder.name(KafkaTopics.dlqFor(KafkaTopics.ACCOUNT_SYNC_COMPLETED))
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic consentRevokedDlqTopic() {
        return TopicBuilder.name(KafkaTopics.dlqFor(KafkaTopics.CONSENT_REVOKED)).partitions(1).replicas(1).build();
    }
}
