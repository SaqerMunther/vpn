package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.eab;

import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.common.ChartAndThreshold;
import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;

public interface ChartsEABCacheableService<T> extends CacheableService<T, Map<String, ChartAndThreshold>> {
	
}
