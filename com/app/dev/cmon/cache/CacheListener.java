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
        CacheService cacheService = CacheService.getInstance();
        logger.info("CacheService initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        CacheService.getInstance().shutdown();
        logger.info("CacheService shutdown");
    }
}
