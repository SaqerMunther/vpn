package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.env.*;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.*;
import org.springframework.transaction.*;

import javax.sql.*;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "oracleSecondEntityManagerFactory", 
    basePackages = {"com.arabbank.hdf.graphdatafetcher.repository.oracle.second"},
    transactionManagerRef = "oracleSecondTransactionManager"
)
public class OracleSecondConfig extends OracleBaseConfig {

    public OracleSecondConfig(Environment environment) {
        super(environment);
    }

    @Primary
    @Bean("oracleSecondDataSource")
    @Override
    public DataSource dataSource() {
        return buildDataSource(
            environment.getProperty("spring.datasource.oracle.second.url"),
            environment.getProperty("spring.datasource.oracle.second.username"),
            environment.getProperty("spring.datasource.oracle.second.password"),
            environment.getProperty("spring.datasource.oracle.second.driver-class-name")
        );
    }

    @Primary
    @Bean("oracleSecondEntityManagerFactory")
    @Override
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setJpaPropertyMap(buildJpaProperties(
            environment.getProperty("spring.datasource.oracle.second.hikari.connection-timeout"),
            environment.getProperty("spring.datasource.oracle.second.hikari.maximum-pool-size"),
            environment.getProperty("spring.datasource.oracle.second.hibernate.default_schema")
        ));
        bean.setPackagesToScan("com.arabbank.hdf.graphdatafetcher.model.entity.oracle.second");
        return bean;
    }

    @Primary
    @Bean("oracleSecondTransactionManager")
    @Override
    public PlatformTransactionManager transactionManager(@Qualifier("oracleSecondEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
