package com.cf.crs.config.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Data
@Configuration
@Primary
@AllArgsConstructor
@NoArgsConstructor
@EnableConfigurationProperties(HeBeIotStatusConfig.class)
@ConfigurationProperties(prefix = "iotconfig")
public class HeBeIotStatusConfig {

    private String  iotIp;
    private String  appId;
    private String  appSecret;
    private String  topic;
    private Boolean encryptTransport;
    private String  tags;
    private String  businessId;

}
