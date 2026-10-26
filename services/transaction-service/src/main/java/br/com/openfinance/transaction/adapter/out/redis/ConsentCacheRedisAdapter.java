package br.com.openfinance.transaction.adapter.out.redis;

import br.com.openfinance.transaction.domain.port.ConsentValidationPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsentCacheRedisAdapter implements ConsentValidationPort {

    private static final String PREFIX = "consent:cache:";

    private final StringRedisTemplate redisTemplate;

    public ConsentCacheRedisAdapter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void validateAuthorised(String consentId, String customerId) {
        String cachedCustomer = redisTemplate.opsForValue().get(PREFIX + consentId);
        if (cachedCustomer == null || !cachedCustomer.equals(customerId)) {
            throw new IllegalArgumentException("Consent not authorised for customer");
        }
    }
}
