package com.app.dev.cmon.cache;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
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

/**
 * Ensures that only the resources created by this web application are shut down,
 * avoiding interference with other applications in the same servlet container.
 */
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

        // 2. Close only the C3P0 pools loaded by this webapp's classloader
        ClassLoader webappClassLoader = getClass().getClassLoader();
        Enumeration<?> pooledDS = C3P0Registry.getPooledDataSources();
        while (pooledDS.hasMoreElements()) {
            Object ds = pooledDS.nextElement();
            if (ds instanceof ComboPooledDataSource) {
                ComboPooledDataSource cpds = (ComboPooledDataSource) ds;
                if (cpds.getClass().getClassLoader() == webappClassLoader) {
                    try {
                        cpds.close();
                        logger.info("Closed C3P0 pool: {}", cpds.getDataSourceName());
                    } catch (Exception e) {
                        logger.warn("Error closing C3P0 pool {}", cpds.getDataSourceName(), e);
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
    }
}




Type mismatch: cannot convert from Set to Enumeration<?>
