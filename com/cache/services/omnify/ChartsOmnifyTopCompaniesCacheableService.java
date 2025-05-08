package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.omnify;

import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.common.omnify.TopCompaniesLineChartSeries;
import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;

public interface ChartsOmnifyTopCompaniesCacheableService<T> extends CacheableService<T, Map<String, TopCompaniesLineChartSeries>> {

}
