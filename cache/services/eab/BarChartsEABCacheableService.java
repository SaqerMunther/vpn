package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.eab;

import java.util.List;
import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.common.BarChartLEntry;
import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;

public interface BarChartsEABCacheableService<T> extends CacheableService<T,Map<String, Map<String, List<BarChartLEntry>>>>  {

}
