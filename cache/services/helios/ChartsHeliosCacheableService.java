package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.helios;

import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.common.LineChartSeries;
import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;

public interface ChartsHeliosCacheableService<T> extends CacheableService<T, Map<String, LineChartSeries>> {

}
