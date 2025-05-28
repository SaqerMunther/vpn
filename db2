package com.arabbank.hdf.cmon.configurations;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef  = "oracleEntityManagerFactory",
    basePackages             = "com.arabbank.hdf.cmon.repository.oracle",
    transactionManagerRef    = "oracleTransactionManager"
)
public class OracleConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.oracle")
    public DataSourceProperties oracleDataSourceProps() {
        return new DataSourceProperties();
    }

    @Bean("oracleDataSource")
    @Primary
    public DataSource oracleDataSource(@Qualifier("oracleDataSourceProps") DataSourceProperties props) {
        return props
                .initializeDataSourceBuilder()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean("oracleEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
            @Qualifier("oracleDataSource") DataSource ds) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("com.arabbank.hdf.cmon.entity.oracle");
        return emf;
    }

    @Bean("oracleTransactionManager")
    @Primary
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}






--------------------------------


  package com.arabbank.hdf.cmon.configurations;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef  = "bridgeSqlServerEntityManagerFactory",
    basePackages             = "com.arabbank.hdf.cmon.repository.bridgeSql",
    transactionManagerRef    = "bridgeSqlServerTransactionManager"
)
public class BridgeSqlServerConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.cmon-bridge-sqlserver")
    public DataSourceProperties bridgeSqlServerProps() {
        return new DataSourceProperties();
    }

    @Bean("bridgeSqlServerDataSource")
    public DataSource bridgeSqlServerDataSource(
            @Qualifier("bridgeSqlServerProps") DataSourceProperties props) {
        return props
                .initializeDataSourceBuilder()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean("bridgeSqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean bridgeSqlServerEMF(
            @Qualifier("bridgeSqlServerDataSource") DataSource ds) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("com.arabbank.hdf.cmon.entity.bridge");
        return emf;
    }

    @Bean("bridgeSqlServerTransactionManager")
    public PlatformTransactionManager bridgeSqlServerTM(
            @Qualifier("bridgeSqlServerEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}





------------------------



package com.arabbank.hdf.cmon.configurations;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef  = "sqlServerEntityManagerFactory",
    basePackages             = "com.arabbank.hdf.cmon.repository.sqlserver",
    transactionManagerRef    = "sqlServerTransactionManager"
)
public class SqlServerConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.sqlserver")
    public DataSourceProperties sqlServerProps() {
        return new DataSourceProperties();
    }

    @Bean("sqlServerDataSource")
    public DataSource sqlServerDataSource(
            @Qualifier("sqlServerProps") DataSourceProperties props) {
        return props
                .initializeDataSourceBuilder()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean("sqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sqlServerEMF(
            @Qualifier("sqlServerDataSource") DataSource ds) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("com.arabbank.hdf.cmon.entity.sqlserver");
        return emf;
    }

    @Bean("sqlServerTransactionManager")
    public PlatformTransactionManager sqlServerTM(
            @Qualifier("sqlServerEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}









-------------------------------







package com.arabbank.hdf.cmon.configurations;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef  = "oracleWriteEntityManagerFactory",
    basePackages             = "com.arabbank.hdf.cmon.repository.oracleWrite",
    transactionManagerRef    = "oracleWriteTransactionManager"
)
public class OracleWriteConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.oracle-write")
    public DataSourceProperties oracleWriteProps() {
        return new DataSourceProperties();
    }

    @Bean("oracleWriteDataSource")
    public DataSource oracleWriteDataSource(
            @Qualifier("oracleWriteProps") DataSourceProperties props) {
        return props
                .initializeDataSourceBuilder()
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();
    }

    @Bean("oracleWriteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleWriteEMF(
            @Qualifier("oracleWriteDataSource") DataSource ds) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("com.arabbank.hdf.cmon.entity.oracle");
        return emf;
    }

    @Bean("oracleWriteTransactionManager")
    public PlatformTransactionManager oracleWriteTM(
            @Qualifier("oracleWriteEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}





-------------------






package com.arabbank.hdf.cmon.configurations;

import java.util.Collections;
import javax.sql.DataSource;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.actuate.metrics.jdbc.DataSourcePoolMetrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class MultiDataSourceMetricsConfig {

    @Bean
    public MeterBinder oracleMetrics(
            @Qualifier("oracleDataSource") DataSource ds) {
        return new DataSourcePoolMetrics(
                ds, Collections.emptyList(), "oracle", Collections.emptyList());
    }

    @Bean
    public MeterBinder bridgeMetrics(
            @Qualifier("bridgeSqlServerDataSource") DataSource ds) {
        return new DataSourcePoolMetrics(
                ds, Collections.emptyList(), "bridgeSqlServer", Collections.emptyList());
    }

    @Bean
    public MeterBinder sqlServerMetrics(
            @Qualifier("sqlServerDataSource") DataSource ds) {
        return new DataSourcePoolMetrics(
                ds, Collections.emptyList(), "sqlServer", Collections.emptyList());
    }

    @Bean
    public MeterBinder oracleWriteMetrics(
            @Qualifier("oracleWriteDataSource") DataSource ds) {
        return new DataSourcePoolMetrics(
                ds, Collections.emptyList(), "oracleWrite", Collections.emptyList());
    }
}
