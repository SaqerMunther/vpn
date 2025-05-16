package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.sme.cardsCache;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

public class LoggingConcurrentMapCacheManager extends ConcurrentMapCacheManager {

    @Override
    protected Cache createConcurrentMapCache(final String name) {
        return new LoggingConcurrentMapCache(name, isAllowNullValues());
    }

    public LoggingConcurrentMapCacheManager(String... cacheNames) {
        super(cacheNames);
    }
}
