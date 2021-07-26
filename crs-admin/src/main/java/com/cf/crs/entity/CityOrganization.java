package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 考评模型
 * @author frank
 * 2019/10/16
 **/
@Data
@TableName("city_organization")
public class CityOrganization implements Serializable {

    private Integer id;
    private Integer code;
    private Integer parent;
    private String organization;
    private String fullname;
    private String description;
    private Integer sequence;
    private Integer isDisabled;
    private Long createAt;
    private Long updateAt;
    private String auth;
    @TableField(exist = false)
    private  List<CityOrganization> children=new ArrayList<>();

}
