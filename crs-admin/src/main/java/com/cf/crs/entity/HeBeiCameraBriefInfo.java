package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("摄像机设备信息列表")
@TableName("hebeismart_camerabriefinfo")
public class HeBeiCameraBriefInfo implements Serializable {
    @ApiModelProperty("设备编码")
    private String code;
    @ApiModelProperty("摄像机名称")
    private String name;
    @ApiModelProperty("所属设备组编码")
    private String deviceGroupCode;
    @ApiModelProperty("父设备组编码")
    private String parentCode;
    @ApiModelProperty("设备归属域的域编码")
    private String domainCode;
    @ApiModelProperty("主设备型号")
    private String deviceModelType;
    @ApiModelProperty("主设备提供商类型")
    private String vendorType;
    @ApiModelProperty("主设备类型")
    private Integer deviceFormType;
    @ApiModelProperty("摄像机类型")
    private Integer type;
    @ApiModelProperty("摄像机安装位置")
    private String cameraLocation;
    @ApiModelProperty("摄像机扩展状态")
    private Long cameraStatus;
    @ApiModelProperty("设备状态")
   private Integer status;
    @ApiModelProperty("网络类型")
   private Integer netType;
    @ApiModelProperty("是否支持智能")
   private Integer isSupportIntellige;
    @ApiModelProperty("是否启用随路音")
   private Integer enableVoice;
    @ApiModelProperty("设备所属nvr编码")
   private String nvrCode;
    @ApiModelProperty("设备创建时间")
   private String deviceCreateTime;
    @ApiModelProperty("是否为外域")
   private Integer isExDomain;
    @ApiModelProperty("前端IP")
   private String deviceIP;
    @ApiModelProperty("保留字段")
   private String reserve;
    @ApiModelProperty("主键")
      private long id;

}
