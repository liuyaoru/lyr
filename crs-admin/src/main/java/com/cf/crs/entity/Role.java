package com.cf.crs.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("role")
public class Role implements Serializable {
    private Integer roleId;
    private String roleName;
    private String remark;
    private Date create_date;
}
