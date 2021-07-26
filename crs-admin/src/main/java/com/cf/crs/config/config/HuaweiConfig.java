package com.cf.crs.config.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Data
@Configuration
@EnableConfigurationProperties(HuaweiConfig.class)
@ConfigurationProperties(prefix = "huaweiconfig")
@Primary
public class HuaweiConfig {

    private String username;
    private String password;
    private String contenttype;
    private String url;
    private String to_index;
    private String from_index;
}
