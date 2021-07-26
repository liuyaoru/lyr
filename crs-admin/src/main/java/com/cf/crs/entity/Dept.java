package com.cf.crs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class Dept implements Serializable {

    private long dept_id;
    private  long  parent_id;
    private String ancestors;
    private String dept_name;
    private String email;
    private String leader;
    /** 子部门 */
    private List<Dept> children = new ArrayList<Dept>();
}
