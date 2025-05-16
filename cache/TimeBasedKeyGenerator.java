package com.arabbank.hdf.digitalbackend.digital.configuration.cache;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.cache.interceptor.KeyGenerator;

public class TimeBasedKeyGenerator implements KeyGenerator{

	@Override
    public Object generate(Object target, Method method, Object... params) {
        return LocalTime.now().truncatedTo(ChronoUnit.MINUTES).toString();
    }
}
