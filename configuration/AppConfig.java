package com.arabbank.hdf.graphdatafetcher.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
@Data
@Validated
public class AppConfig {
    private Kvstore kvstore;
    private Utc utc;
    private Kafka kafka;

    @Data
    public static class Kvstore {
        private Keys keys;

        @Data
        public static class Keys {
            private String timeFetching;
        }
    }

    @Data
    public static class Utc {
        private int offset;
    }

    @Data
    public static class Kafka {
        private Topic topic;
        private Schedule schedule;

        @Data
        public static class Topic {
            private String transaction;
            private String latency;
            private String endOfDay;
            private String readinessTopic;
            private String userData;
        }

        @Data
        public static class Schedule {
            private Users users;
            private Graph graph;

            @Data
            public static class Users {
                private Long repeatEveryMs;
            }

            @Data
            public static class Graph {
                private Long repeatEveryMs;
            }

            private Long fetchingBeforeMs;
        }
    }
}