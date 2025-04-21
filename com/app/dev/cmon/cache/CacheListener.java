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





package com.app.dev.cmon.cache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class CacheContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Force CacheService to initialize on startup
        CacheService cacheService = CacheService.getInstance();
        logger.info("CacheService initialized on deploy: cache size = {}", cacheService.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanly shut down Ehcache when the webapp is undeployed or server stops
        CacheService.getInstance().shutdown();
        logger.info("CacheService shutdown on undeploy");
    }
}



<listener>
  <listener-class>com.app.dev.cmon.cache.CacheContextListener</listener-class>
</listener>
