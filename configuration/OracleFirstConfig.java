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

import javax.sql.*;

@Configuration
@EnableJpaRepositories (
    entityManagerFactoryRef = "oracleFirstEntityManagerFactory", 
    basePackages = {"com.arabbank.hdf.graphdatafetcher.repository.oracle.first"},
    transactionManagerRef = "oracleFirstTransactionManager"
)
public class OracleFirstConfig extends OracleBaseConfig {


    public OracleFirstConfig(Environment environment) {
        super(environment);
    }

    @Primary
    @Bean("oracleFirstDataSource")
    @Override
    public DataSource dataSource() {
        return buildDataSource(
            environment.getProperty("spring.datasource.oracle.first.url"),
            environment.getProperty("spring.datasource.oracle.first.username"),
            environment.getProperty("spring.datasource.oracle.first.password"),
            environment.getProperty("spring.datasource.oracle.first.driver-class-name")
        );
    }

    @Primary
    @Bean("oracleFirstEntityManagerFactory")
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setJpaPropertyMap(buildJpaProperties(
            environment.getProperty("spring.datasource.oracle.first.hikari.connection-timeout"),
            environment.getProperty("spring.datasource.oracle.first.hikari.maximum-pool-size"),
            null
        ));
        bean.setPackagesToScan("com.arabbank.hdf.graphdatafetcher.model.entity.oracle.first");
        return bean;
    }

    @Primary
    @Bean("oracleFirstTransactionManager")
    @Override
    public PlatformTransactionManager transactionManager(@Qualifier("oracleFirstEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    @Primary
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("oracleFirstDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
