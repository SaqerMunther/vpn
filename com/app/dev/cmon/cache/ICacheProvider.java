package com.app.dev.cmon.cache;

public interface ICacheProvider {
    <T> T get(String key);
    <T> void put(String key, T value);
    void clear();
    int size();
}
