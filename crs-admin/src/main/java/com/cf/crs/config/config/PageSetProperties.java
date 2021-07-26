package com.cf.crs.config.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="pageset")
@Data
public class PageSetProperties {

    private  String current_page;

    private String every_page;


}
