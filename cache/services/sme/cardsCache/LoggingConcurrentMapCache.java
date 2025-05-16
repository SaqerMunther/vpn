package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.sme.cardsCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class LoggingConcurrentMapCache extends ConcurrentMapCache {

    private static final Logger logger = LoggerFactory.getLogger(LoggingConcurrentMapCache.class);

    public LoggingConcurrentMapCache(String name) {
        super(name);
    }

    public LoggingConcurrentMapCache(String name, boolean allowNullValues) {
        super(name, allowNullValues);
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (valueWrapper != null) {
            logger.info("[Cache Hit] Cache: '{}', Key: '{}'", this.getName(), key);
        } else {
            logger.info("[Cache Miss] Cache: '{}', Key: '{}'", this.getName(), key);
        }
        return valueWrapper;
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        logger.info("[Cache Put] Cache: '{}', Key: '{}'", this.getName(), key);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper oldValue = super.putIfAbsent(key, value);
        boolean insertedNewEntry = (oldValue == null);
        logger.info("[Cache PutIfAbsent] Cache: '{}', Key: '{}', InsertedNewEntry={}",
                     this.getName(), key, insertedNewEntry);
        return oldValue;
    }
}
