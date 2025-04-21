package com.app.dev.cmon.cache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CacheListener implements ServletContextListener {
    private CacheService cacheService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize the cache service when the context is initialized
        cacheService = CacheService.getInstance();
        System.out.println("CacheService initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Clear the cache and shutdown the cache manager when the context is destroyed
        if (cacheService != null) {
            cacheService.clearCache();
            cacheService.shutdown(); // Ensure to shutdown the CacheManager
            System.out.println("CacheService cleared and shutdown.");
        }
    }
}
