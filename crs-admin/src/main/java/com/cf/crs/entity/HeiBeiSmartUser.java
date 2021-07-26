package com.cf.crs.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "统一运维中心用户")
@TableName("hebeismart_user")
public class HeiBeiSmartUser implements Serializable {
    private long userId;
    @ApiModelProperty(value = "性别")
    private Integer gender;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "电话")
    private String userMobile;
    @ApiModelProperty(value = "邮箱")
    private String userMail;
    @ApiModelProperty(value = "办公电话")
    private String userOph;
    @ApiModelProperty(value = "备注")
    private String note;
    @ApiModelProperty(value = "密码")
    private String password="abcd123QAZ";
    @ApiModelProperty(value = "监控子系统权限设置")
   private String NetPrivilege="Operator";
    @ApiModelProperty(value ="运维管理子系统权限设置")
    private String auth="1";
    @ApiModelProperty(value ="Itsm管理子系统权限设置")
   private String ItsPrivilege="SDGuest";
    @ApiModelProperty(value = "部门编号")
   private String organization="214";
    @ApiModelProperty(value = "角色")
    private String roleIds="4";
   @ApiModelProperty(value = "部门id")
    private String departIds="0";




}
