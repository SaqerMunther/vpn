package com.app.dev.cmon.cache;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.PooledDataSource;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;

@WebListener
public class CacheContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize cache service (and any other singletons)
        CacheService.getInstance();
        logger.info("CacheService initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1. Shutdown Ehcache
        CacheService.getInstance().shutdown();
        logger.info("CacheService shutdown");

        // 2. Properly destroy each C3P0 pool (closes connections AND stops helper threads)
        ClassLoader webappClassLoader = getClass().getClassLoader();
        Set<?> pooledDataSources = C3P0Registry.getPooledDataSources();
        for (Object ds : pooledDataSources) {
            if (ds instanceof PooledDataSource) {
                PooledDataSource pds = (PooledDataSource) ds;
                if (pds.getClass().getClassLoader() == webappClassLoader) {
                    try {
                        DataSources.destroy(pds);
                        logger.info("Destroyed C3P0 pool: {}", pds);
                    } catch (Exception e) {
                        logger.warn("Error destroying C3P0 pool {}", pds, e);
                    }
                }
            }
        }

        // 3. Deregister only the JDBC drivers loaded by this webapp
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == webappClassLoader) {
                try {
                    DriverManager.deregisterDriver(driver);
                    logger.info("Deregistered JDBC driver: {}", driver);
                } catch (SQLException ex) {
                    logger.warn("Error deregistering driver {}", driver, ex);
                }
            }
        }

        logger.info("Context destruction completed.");
    }
}
