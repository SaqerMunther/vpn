package com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.omnify;

import java.util.Map;
import com.arabbank.hdf.digitalbackend.digital.configuration.cache.services.CacheableService;

public interface ProductsCacheableService<T> extends CacheableService<T, Map<String,  Map<String, Object>>>{

}
