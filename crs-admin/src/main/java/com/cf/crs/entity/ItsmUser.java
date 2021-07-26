package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItsmUser implements Serializable {
    private String name;
    private Boolean is_vipuser;
    private String requester_allowed_to_view;
    private String login_name;
    private String password;

}
