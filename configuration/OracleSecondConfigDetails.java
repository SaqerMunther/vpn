package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.annotation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "oracle-db.second")
@Data
@Slf4j
public class OracleSecondConfigDetails {
    private String ip;
    private String port;
    private String username;

    @PostConstruct
    public void configLog() {
        log.info(String.format("Oracle (Second) database IP: %s", ip));
        log.info(String.format("Oracle (Second) database port: %s", port));
        log.info(String.format("Oracle (Second) database username: %s", username));
    }
}