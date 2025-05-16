package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services;

public interface CacheableService<T, V> {
	
    T getCacheKey();

    V findInstanceData(String ip);

    String getCacheName();

    V findAllCached(String ip);
}
