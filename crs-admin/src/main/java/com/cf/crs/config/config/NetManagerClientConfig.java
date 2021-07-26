package com.cf.crs.config.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(NetManagerClientConfig.class)
@ConfigurationProperties(prefix = "netmanagerclient")
public class NetManagerClientConfig {
    private String apikey;
    private String url;
}
