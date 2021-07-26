package com.cf.crs.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@Data
@ApiModel(value = "监控系统用户")
public class NetManagerUser implements Serializable {


    private String id;
     private String groupName ;
     private String DOMAINNAME;
     private String USERNAME;
     private String USERID;


}
