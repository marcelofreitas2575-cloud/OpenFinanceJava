package br.com.openfinance.events;

public final class KafkaTopics {

    public static final String CONSENT_CREATED = "of.consent.created.v1";
    public static final String CONSENT_REVOKED = "of.consent.revoked.v1";
    public static final String ACCOUNT_SYNC_COMPLETED = "of.account.sync.completed.v1";
    public static final String ACCOUNT_SYNC_FAILED = "of.account.sync.failed.v1";

    public static String dlqFor(String mainTopic) {
        return mainTopic + ".dlq";
    }

    private KafkaTopics() {
    }
}
