package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("摄像机设备信息列表")
@TableName("hebeismart_iotdevice")
public class HeBeIotDevice implements Serializable {
    private String gatewayld;
    private String deviceId;
    private String deviceName;
    private String status;
    private String deviceType;
    private String subSystem;
    private String mac;
    private String protocolType;
    private String model;
    private String manufacturerId;
    private String manufacturerName;
    private String location;
    private Date createTime=new Date();
    private Date lastUpdateTime=new Date();

}
