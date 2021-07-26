package com.cf.crs.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


import java.io.Serializable;

@Data
@TableName("hebeismart_mail_config")
public class HeBeiSmartSystemConfig implements Serializable {

    @ApiModelProperty(value = "服务器名")
    private String MAIL_SERVER_NAME;
    @ApiModelProperty(value = "端口")
    private String MAIL_PORT;
    @ApiModelProperty(value = "超时")
    private long MAIL_TIMEOUT;
    @ApiModelProperty(value = "发送邮箱")
    private String SEND_EMAIL;
    @ApiModelProperty(value = "接收邮箱")
    private  String RECEIVE_EMAIL;
    @ApiModelProperty(value = "用户名")
    private  String MAIL_USERNAME;
    @ApiModelProperty(value = "密码")
    private String MAIL_PASSWORD;
    @ApiModelProperty(value = "安全连接信息")
    private Integer MAIL_SECURITY;
    @ApiModelProperty(value = "是否添加备用服务器")
    private Boolean IFADDREADYMAIL=false;


}
