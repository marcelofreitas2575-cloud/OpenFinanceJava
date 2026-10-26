package br.com.openfinance.account.adapter.out.redis;

import br.com.openfinance.account.domain.port.ConsentValidationPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ConsentCacheRedisAdapter implements ConsentValidationPort {

    private static final String PREFIX = "consent:cache:";

    private final StringRedisTemplate redisTemplate;

    public ConsentCacheRedisAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheAuthorised(String consentId, String customerId) {
        redisTemplate.opsForValue().set(PREFIX + consentId, customerId, Duration.ofHours(24));
    }

    public void evict(String consentId) {
        redisTemplate.delete(PREFIX + consentId);
    }

    @Override
    public void validateAuthorised(String consentId, String customerId) {
        String cachedCustomer = redisTemplate.opsForValue().get(PREFIX + consentId);
        if (cachedCustomer == null || !cachedCustomer.equals(customerId)) {
            throw new IllegalArgumentException("Consent not authorised for customer");
        }
    }
}
