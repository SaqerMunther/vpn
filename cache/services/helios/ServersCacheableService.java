package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.helios;

import java.util.Map;

import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;
import com.arabbank.hdf.digitalbackend.digital.dto.ServerBoxDTO;

public interface ServersCacheableService<T> extends CacheableService<T, Map<String, ServerBoxDTO>> {

}
