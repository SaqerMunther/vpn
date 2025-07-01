package com.arabbank.hdf.graphdatafetcher.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kafka")
@Data
@Slf4j
public class KafkaConfig {
    private String socket;

    @PostConstruct
    public void configLog() {
        log.info(String.format("Kafka socket(s): %s", socket));
    }
}