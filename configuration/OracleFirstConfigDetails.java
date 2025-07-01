package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "oracle-db.first")
@Data
@Slf4j
public class OracleFirstConfigDetails {
    private String ip;
    private String port;
    private String username;

    @PostConstruct
    public void configLog() {
        log.info(String.format("Oracle (First) database IP: %s", ip));
        log.info(String.format("Oracle (First) database port: %s", port));
        log.info(String.format("Oracle (First) database username: %s", username));
    }
}