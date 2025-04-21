package com.app.dev.cmon.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheService {
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private static CacheService instance;
    private ICacheProvider cacheProvider;
    private CacheConfig cacheConfig;
    private long lastSyncID;
    
    private CacheService() {
        cacheConfig = new CacheConfig();
        cacheProvider = new EhcacheProvider(cacheConfig.getDataCache());
        logger.info("RADAR CacheService initialized.");
    }

    public static synchronized CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }

    public String getCacheKey(String methodName, String extra, int viewId, int subViewId, String country, String date) {
        return (methodName + "_" + extra + "_" + viewId + "_" +
                (country.equalsIgnoreCase("") ? "all" : country) + "_" + subViewId + "_" + date).toLowerCase();
    }

    public <T> T getFromCache(String key) {
        return cacheProvider.get(key);
    }

    public <T> void putInCache(String key, T value) {
        cacheProvider.put(key, value);
        logger.debug("Saved cache entry with key: {}", key);
    }

    public void clearCache() {
        cacheProvider.clear();
        logger.info("RADAR Cache cleared.");
    }

    public void shutdown() {
        cacheConfig.shutdown();
    }

    public long getLastSyncID() {
        return lastSyncID;
    }

    public void setLastSyncID(long lastSyncID) {
        this.lastSyncID = lastSyncID;
    }
}
