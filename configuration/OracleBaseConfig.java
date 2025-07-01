package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.persistence.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.jdbc.datasource.*;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.*;
import org.springframework.transaction.annotation.*;

import javax.sql.*;
import java.util.*;

@Configuration
@EnableTransactionManagement
public abstract class OracleBaseConfig {

    protected final Environment environment;

    protected OracleBaseConfig(Environment environment) {
        this.environment = environment;
    }

    public abstract DataSource dataSource();

    public abstract LocalContainerEntityManagerFactoryBean entityManagerFactoryBean();

    @Bean({"oracleFirstTransactionManager", "oracleSecondTransactionManager", "sqlServerTransactionManager"})
    public abstract PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory);

    protected DriverManagerDataSource buildDataSource(String url, String username, String password, String driverClassName) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    protected Map<String, Object> buildJpaProperties(String connectionTimeout, String maxPoolSize, String schema) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hikari.connection-timeout", connectionTimeout);
        properties.put("hikari.maximum-pool-size", maxPoolSize);
        if (schema != null) {
            properties.put("hibernate.default_schema", schema);
        }
        return properties;
    }

}