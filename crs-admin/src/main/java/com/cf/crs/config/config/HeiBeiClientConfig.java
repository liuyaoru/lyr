package com.cf.crs.config.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@EnableConfigurationProperties(HeiBeiClientConfig.class)
@ConfigurationProperties(prefix = "client2")
@Primary
public class HeiBeiClientConfig {
    private String url;

    private String clientId;

    private String clientSecret;

    private String responseType;

    private String  grantType;

    private String clientgrantType;
    private String redirectUri;



}
