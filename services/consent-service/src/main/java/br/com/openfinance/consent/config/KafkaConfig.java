package br.com.openfinance.consent.config;

import br.com.openfinance.events.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic consentCreatedTopic() {
        return TopicBuilder.name(KafkaTopics.CONSENT_CREATED).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic consentRevokedTopic() {
        return TopicBuilder.name(KafkaTopics.CONSENT_REVOKED).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic consentCreatedDlqTopic() {
        return TopicBuilder.name(KafkaTopics.dlqFor(KafkaTopics.CONSENT_CREATED)).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic consentRevokedDlqTopic() {
        return TopicBuilder.name(KafkaTopics.dlqFor(KafkaTopics.CONSENT_REVOKED)).partitions(1).replicas(1).build();
    }

    @Bean
    NewTopic accountSyncCompletedDlqTopic() {
        return TopicBuilder.name(KafkaTopics.dlqFor(KafkaTopics.ACCOUNT_SYNC_COMPLETED)).partitions(1).replicas(1).build();
    }
}
