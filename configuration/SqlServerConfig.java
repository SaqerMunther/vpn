package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.transaction.*;

import java.util.Map;

import javax.sql.*;

@Configuration
@EnableJpaRepositories (
        entityManagerFactoryRef = "sqlServerEntityManagerFactory",
        basePackages = {"com.arabbank.hdf.graphdatafetcher.repository.sqlserver"},
        transactionManagerRef = "sqlServerTransactionManager"
)
public class SqlServerConfig extends OracleBaseConfig {

    public SqlServerConfig(Environment environment) {
        super(environment);
    }

    @Bean("sqlServerDataSource")
    @Override
    public DataSource dataSource() {
        return buildDataSource(
                environment.getProperty("spring.datasource.sqlserver.url"),
                environment.getProperty("spring.datasource.sqlserver.username"),
                environment.getProperty("spring.datasource.sqlserver.password"),
                environment.getProperty("spring.datasource.sqlserver.driver-class-name")
        );
    }

    @Bean("sqlServerEntityManagerFactory")
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = buildJpaProperties(
            environment.getProperty("spring.datasource.sqlserver.hikari.connection-timeout"),
            environment.getProperty("spring.datasource.sqlserver.hikari.maximum-pool-size"),
            environment.getProperty("spring.datasource.sqlserver.schema")
        );
        props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");

        bean.setJpaPropertyMap(props);
        bean.setPackagesToScan("com.arabbank.hdf.graphdatafetcher.model.entity.sqlserver");
        return bean;
    }

    @Bean("sqlServerTransactionManager")
    @Override
    public PlatformTransactionManager transactionManager(@Qualifier("sqlServerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean("sqlServerNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate sqlServerNamedParameterJdbcTemplate(@Qualifier("sqlServerDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}