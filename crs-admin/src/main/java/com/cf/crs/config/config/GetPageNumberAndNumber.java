package com.cf.crs.config.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@EnableConfigurationProperties(com.cf.crs.config.config.PageSetProperties.class)
public class GetPageNumberAndNumber {
    @Autowired
  private PageSetProperties pageSetProperties;


  public String GetCurrent()
  {
return  pageSetProperties.getCurrent_page();
  }
  public  String GetEveryNumber()
  {

      return  pageSetProperties.getEvery_page();
  }

}
