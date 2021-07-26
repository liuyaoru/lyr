package com.cf.crs.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("河北智慧园区组织机构列表")
@TableName("hebeismart_organization")
public class HeBeiSmartOrganization {
    private long orgId;
    private long parentId;
    private String orgName;
    private String orgCode;
    private String  orderNo;
    private Integer orgType;
    private String note;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
    @TableField(exist = false)
    private List<HeBeiSmartOrganization> children=new ArrayList<>();


}
