package com.app.dev.cmon.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheConfig {
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);
    private CacheManager cacheManager;

    public CacheConfig() {
        Configuration config = new Configuration();
        
        // Set the cache to be eternal
        CacheConfiguration cacheConfig = new CacheConfiguration("dataCache", 4000)
                .eternal(true) // Set to true for eternal cache entries
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU);
        
        config.addCache(cacheConfig);
        cacheManager = CacheManager.newInstance(config);
        logger.debug("CacheConfig initialized.");
    }

    public Cache getDataCache() {
        return cacheManager.getCache("dataCache");
    }

    public void shutdown() {
        cacheManager.shutdown();
        logger.debug("CacheConfig shutdown.");
    }
}
